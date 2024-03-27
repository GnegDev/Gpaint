package com.gnegdev.gpaint.utils;

public class DragContext {
        private double startX = 0;
        private double startY = 0;
        private double endX = 0;
        private double endY = 0;

        public double getStartX() {
                return startX;
        }

        public void setStartX(double startX) {
                this.startX = startX;
        }

        public double getStartY() {
                return startY;
        }

        public void setStartY(double startY) {
                this.startY = startY;
        }

        public double getEndX() {
                return endX;
        }

        public void setEndX(double endX) {
                this.endX = endX;
        }

        public double getEndY() {
                return endY;
        }

        public void setEndY(double endY) {
                this.endY = endY;
        }
}
