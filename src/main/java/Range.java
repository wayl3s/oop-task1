import java.util.Objects;
import java.util.function.Function;

public class Range<C extends Comparable<C>> implements Comparable<Range<C>>, Cloneable{
    private C left;
    private C right;
    private RangeType leftType;
    private RangeType rightType;
    public enum RangeType {OPEN, CLOSED};

    public Range<C> range(C left, C right, RangeType leftType, RangeType rightType) {
        if (left.equals(right)) {
            if (leftType == RangeType.CLOSED && rightType == RangeType.CLOSED) {
                this.left = left;
                this.right = right;
                this.leftType = leftType;
                this.rightType = rightType;

            } else {
                return null;
            }
        }
        if (left.compareTo(right) < 0) {
            this.left = left;
            this.right = right;
            this.leftType = leftType;
            this.rightType = rightType;
        } else {
            this.left = right;
            this.right = left;
            this.leftType = rightType;
            this.rightType = leftType;
        }
        return this;
    }

    public Range<C> open(C left, C right) {
        if (left.equals(right)) {
            return null;
        }
        if (left.compareTo(right) < 0) {
            this.left = left;
            this.right = right;
        } else {
            this.left = right;
            this.right = left;
        }
        this.leftType = RangeType.OPEN;
        this.rightType = RangeType.OPEN;

        return this;
    }

    public Range<C> openClosed(C left, C right) {
        if (left.equals(right)) {
            return null;
        }
        if (left.compareTo(right) < 0) {
            this.left = left;
            this.right = right;
            this.leftType = RangeType.OPEN;
            this.rightType = RangeType.CLOSED;
        } else {
            this.left = right;
            this.right = left;
            this.leftType = RangeType.CLOSED;
            this.rightType = RangeType.OPEN;
        }
        
        return this;
    }

    public Range<C> closedOpen(C left, C right) {
        if (left.equals(right)) {
            return null;
        }
        if (left.compareTo(right) < 0) {
            this.left = left;
            this.right = right;
            this.leftType = RangeType.CLOSED;
            this.rightType = RangeType.OPEN;
        } else {
            this.left = right;
            this.right = left;
            this.leftType = RangeType.OPEN;
            this.rightType = RangeType.CLOSED;
        }

        return this;
    }

    public Range<C> closed(C left, C right) {
        if (left.compareTo(right) <= 0) {
            this.left = left;
            this.right = right;
        } else {
            this.left = right;
            this.right = left;
        }
        this.leftType = RangeType.CLOSED;
        this.rightType = RangeType.CLOSED;

        return this;
    }

    public C getLeft() {
        return this.left;
    }
    
    public C getRight() {
        return this.right;
    }

    public RangeType getLeftType() {
        return this.leftType;
    }
    
    public RangeType getRightType() {
        return this.rightType;
    }


    public boolean has(C point) {
        if (point != null) {
            if ((point.compareTo(this.left) > 0 || (point.equals(this.left) && this.leftType == RangeType.CLOSED))
            && (point.compareTo(this.right) < 0 || (point.equals(this.right) && this.rightType == RangeType.CLOSED))) {
                return true;
            }
        }
        return false;
    }

    public boolean connects(Range<C> range) {
        if (range.right.compareTo(this.left) < 0 
            || range.left.compareTo(this.right) > 0 
            || (range.right.equals(this.left) && this.leftType == RangeType.OPEN && range.rightType == RangeType.OPEN) 
            || (range.left.equals(this.right) && this.rightType == RangeType.OPEN && range.leftType == RangeType.OPEN)) {
            return false;
        }
        return true;
    }

    public void enclose(Range<C> range) {
        if (range == null) {
            return;
        }
        if (this.left.compareTo(range.left) > 0) {
            this.left = range.left;
            this.leftType = range.leftType;
        } else if (this.left.equals(range.left) && (this.leftType == RangeType.CLOSED || range.leftType == RangeType.CLOSED)) {
            this.leftType = RangeType.CLOSED;
        }
        if (this.right.compareTo(range.right) < 0) {
            this.right = range.right;
            this.rightType = range.rightType;
        } else if (this.right.equals(range.right) && (this.rightType == RangeType.CLOSED || range.rightType == RangeType.CLOSED)) {
            this.rightType = RangeType.CLOSED;
        }
    }
    
    @SuppressWarnings("unchecked")
    public Range<C>[] substract(Range<C> range) {
        if (range == null) {
            return null;
        }

        if (this.compareTo(range) != 0) {
            return null;
        }

        Range<C> leftRange = null;
        Range<C> rigthRange = null;

        if (range.left.compareTo(this.left) > 0 || (range.left.equals(this.left) && this.leftType == RangeType.CLOSED && range.leftType == RangeType.OPEN)) {
            leftRange = new Range<C>().range(
                this.left,
                range.left,
                this.leftType, 
                range.leftType == RangeType.OPEN ? RangeType.CLOSED: RangeType.OPEN
            );
        }

        if (range.right.compareTo(this.right) < 0 || (range.right.equals(this.right) && this.rightType == RangeType.CLOSED && range.rightType == RangeType.OPEN)) {
            rigthRange = new Range<C>().range(
                range.right,
                this.right,
                range.rightType == RangeType.OPEN ? RangeType.CLOSED: RangeType.OPEN, 
                this.rightType
            );
        }

        return new Range[]{leftRange, rigthRange};
    }

    public String toString() {
        return String.format("%s%s; %s%s", 
            this.leftType == RangeType.OPEN ? "(":"[", this.left.toString(), 
            this.right.toString(), this.rightType == RangeType.OPEN ? ")":"]");
    }

    public Range<C> fromString(String rangeString, Function<String, C> parser) {
        if (rangeString == null || rangeString.trim().isEmpty()) {
            throw new IllegalArgumentException("Range string cannot be null or empty");
        }
        
        String trimmed = rangeString.trim();
        
        if (!trimmed.matches("^[\\[\\(].*?;.*?[\\]\\)]$")) {
            throw new IllegalArgumentException("Invalid range format: " + rangeString);
        }
        
        char leftBound = trimmed.charAt(0);
        char rightBound = trimmed.charAt(trimmed.length() - 1);
        
        if (leftBound != '(' && leftBound != '[') {
            throw new IllegalArgumentException("Left boundary must be '(' or '['");
        }
        
        if (rightBound != ')' && rightBound != ']') {
            throw new IllegalArgumentException("Right boundary must be ')' or ']'");
        }
        
        String content = trimmed.substring(1, trimmed.length() - 1).trim();
        String[] parts = content.split(";");
        
        if (parts.length != 2) {
            throw new IllegalArgumentException("Range must contain exactly one ';' separator");
        }
        
        String leftValueStr = parts[0].trim();
        String rightValueStr = parts[1].trim();
        
        if (leftValueStr.isEmpty() || rightValueStr.isEmpty()) {
            throw new IllegalArgumentException("Range values cannot be empty");
        }
        
        RangeType leftType = (leftBound == '(') ? RangeType.OPEN : RangeType.CLOSED;
        RangeType rightType = (rightBound == ')') ? RangeType.OPEN : RangeType.CLOSED;
        
        C leftValue = parser.apply(leftValueStr);
        C rightValue = parser.apply(rightValueStr);
        
        return this.range(leftValue, rightValue, leftType, rightType);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Range<C> clone() throws CloneNotSupportedException {
        return (Range<C>) super.clone();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || this.getClass() != object.getClass()) return false;
        return this.left.equals(((Range<C>) object).left) 
            && this.right.equals(((Range<C>) object).right) 
            && this.leftType == ((Range<C>) object).leftType 
            && this.rightType == ((Range<C>) object).rightType;
    }

    public int hashCode() {
        return Objects.hash(left, right, leftType, rightType);
    }

    @Override
    public int compareTo(Range<C> range) {
        if (range.right.compareTo(this.left) < 0 || (range.right.equals(this.left) 
            && (this.leftType == RangeType.OPEN || range.rightType == RangeType.OPEN))) {
            return 1;
        }
        if (range.left.compareTo(this.right) > 0 || (range.left.equals(this.right) 
            && (this.rightType == RangeType.OPEN || range.leftType == RangeType.OPEN))) {
            return -1;
        }
        return 0;
    }
}