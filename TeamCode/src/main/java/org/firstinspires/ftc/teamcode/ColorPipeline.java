    private static char checkForHSV(double h, double error) {
        assert h <= 60;
        double horiz_rot = (h-60) % 360;

        //red = 300
        if (300-error < horiz_rot && horiz_rot < 300+error) {
            return 'r';
        }
        //green = 60
        if (60-error < horiz_rot && horiz_rot < 60+error) {
            return 'g';
        }
        //blue = 180
        if (180-error < horiz_rot && horiz_rot < 180+error) {
            return 'b';
        }

        return 'e';
    }

    