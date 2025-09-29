public class Range implements Comparable<Range> {
    private double left;
    private double right;
    private RangeType leftType;
    private RangeType rightType;
    private enum RangeType {OPEN, CLOSED};

    // public Range(double left, double right, RangeType type) {
    //     this.left = left;
    //     this.right = right;
    //     this.type = type;
    // }

    public Range open(double left, double right) {
        if (left == right) {
            return null;
        }
        if (left <= right) {
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

    public Range openClosed(double left, double right) {
        if (left == right) {
            return null;
        }
        if (left <= right) {
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

    public Range closedOpen(double left, double right) {
        if (left == right) {
            return null;
        }
        if (left <= right) {
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

    public Range closed(double left, double right) {
        if (left <= right) {
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

    public double getLeftBound() {
        return this.left;
    }
    
    public double getRightBound() {
        return this.left;
    }

    public double getLeftBoundType() {
        return this.left;
    }
    
    public double getRightBoundType() {
        return this.left;
    }

    public String toString() {
        return String.format("%s%,.2f, %,.2f%s", this.leftType == RangeType.OPEN ? "(":"[", this.left , this.right, this.rightType == RangeType.OPEN ? ")":"]" );
    }

    public boolean isIn(double point) {
        if ((point > this.left || (point == this.left && this.leftType == RangeType.CLOSED)) && (point < this.right || (point == this.right && this.rightType == RangeType.CLOSED))) {
            return true;
        }
        return false;
    }

    public void bracket(Range range) {
        if (this.left > range.left) {
            this.left = range.left;
            this.leftType = range.leftType;
        } else if (this.left == range.left && (this.leftType == RangeType.CLOSED || range.leftType == RangeType.CLOSED)) {
            this.leftType = RangeType.CLOSED;
        }
        if (this.right < range.right) {
            this.right = range.right;
            this.rightType = range.rightType;
        } else if (this.right == range.right && (this.rightType == RangeType.CLOSED || range.rightType == RangeType.CLOSED)) {
            this.rightType = RangeType.CLOSED;
        }
    }

    @Override
    public int compareTo(Range range) {
        if (range.right < this.left || (range.right == this.left && (this.leftType == RangeType.OPEN || range.rightType == RangeType.OPEN))) {
            return 1;
        }
        if (range.left > this.right || (range.left == this.right && (this.rightType == RangeType.OPEN || range.leftType == RangeType.OPEN))) {
            return -1;
        }
        return 0;
    }
}