package main.java;

public class Range<C extends Comparable<C>> implements Comparable<Range<C>>, Cloneable{
    private C left;
    private C right;
    private RangeType leftType;
    private RangeType rightType;
    public enum RangeType {OPEN, CLOSED};

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

    public C getLeftBound() {
        return this.left;
    }
    
    public C getRightBound() {
        return this.right;
    }

    public RangeType getLeftBoundType() {
        return this.leftType;
    }
    
    public RangeType getRightBoundType() {
        return this.rightType;
    }

    public String toString() {
        return String.format("%s%s; %s%s", this.leftType == RangeType.OPEN ? "(":"[", this.left.toString() , this.right.toString(), this.rightType == RangeType.OPEN ? ")":"]" );
    }

    public boolean isIn(C point) {
        if ((point.compareTo(this.left) > 0 || (point.equals(this.left) && this.leftType == RangeType.CLOSED))
        && (point.compareTo(this.right) > 0 || (point.equals(this.right) && this.rightType == RangeType.CLOSED))) {
            return true;
        }
        return false;
    }

    public void enclose(Range<C> range) {
        if (this.left.compareTo(range.left) > 0) {
            this.left = range.left;
            this.leftType = range.leftType;
        } else if (this.left.equals(range.left) && (this.leftType == RangeType.CLOSED || range.leftType == RangeType.CLOSED)) {
            this.leftType = RangeType.CLOSED;
        }
        if (this.right.compareTo(range.right) < 0) {
            this.right = range.right;
            this.rightType = range.rightType;
        } else if (this.right == range.right && (this.rightType == RangeType.CLOSED || range.rightType == RangeType.CLOSED)) {
            this.rightType = RangeType.CLOSED;
        }
    }
    // Will return second range if the argument splits the main range, otherwise null
    public Range<C> difference(Range<C> range) {
        if ((range.left.compareTo(this.left) > 0 || (range.right.equals(this.left) && (this.leftType == RangeType.CLOSED && range.leftType == RangeType.CLOSED))) 
        && (range.right.compareTo(this.right) < 0 || (range.right.equals(this.right) && (this.rightType == RangeType.CLOSED && range.rightType == RangeType.CLOSED)))) {
            Range<C> returnRange = new Range<C>();
            if (range.rightType == this.rightType) {
                returnRange.closed(range.right, this.right);
            } else if (range.rightType == RangeType.CLOSED && this.rightType == RangeType.OPEN) {
                returnRange.closedOpen(range.right, this.right);
            } else if (range.rightType == RangeType.OPEN && this.rightType == RangeType.CLOSED) {
                returnRange.openClosed(range.right, this.right);
            }
            this.right = range.left;
            this.rightType = range.leftType == RangeType.CLOSED ? RangeType.OPEN: RangeType.CLOSED;
            return returnRange;
        } else if (((range.left.compareTo(this.left) > 0) && (range.left.compareTo(this.right) < 0))
        || ((range.left.equals(this.left) && (this.leftType == RangeType.CLOSED && range.leftType == RangeType.CLOSED)) || ((range.left.equals(this.right)) && (this.rightType == RangeType.CLOSED && range.leftType == RangeType.CLOSED)))) {
            this.right = range.left;
            this.rightType = range.leftType == RangeType.CLOSED ? RangeType.OPEN: RangeType.CLOSED;
        } else if (((range.right.compareTo(this.left) > 0) && (range.right.compareTo(this.right) < 0))
        || ((range.right.equals(this.left) && (this.leftType == RangeType.CLOSED && range.rightType == RangeType.CLOSED)) || ((range.right.equals(this.right))) && (this.rightType == RangeType.CLOSED && range.rightType == RangeType.CLOSED))) {
            this.left = range.right;
            this.leftType = range.rightType == RangeType.CLOSED ? RangeType.OPEN: RangeType.CLOSED;
        }
        return null;
    }

    public boolean equals(Range<C> range) {
        return this.left.equals(range.left) && this.right.equals(range.right) && this.leftType == range.leftType && this.rightType == range.rightType;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Range<C> clone() throws CloneNotSupportedException {
        return (Range<C>) super.clone();
    }

    @Override
    public int compareTo(Range<C> range) {
        if (range.right.compareTo(this.left) < 0 || (range.right.equals(this.left) && (this.leftType == RangeType.OPEN || range.rightType == RangeType.OPEN))) {
            return 1;
        }
        if (range.left.compareTo(this.right) > 0 || (range.left.equals(this.right) && (this.rightType == RangeType.OPEN || range.leftType == RangeType.OPEN))) {
            return -1;
        }
        return 0;
    }
}