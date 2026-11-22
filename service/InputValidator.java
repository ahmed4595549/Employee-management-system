package service;

public final class InputValidator {

    private InputValidator() {
    }
    // WE DONNOT NEED AN OBJECT FROM IT OUTSIDE WE WILL NOT CREAT A ONE

    public static String requireNotEmpty(
            String value, String fieldName
    ) throws ValidationException {

        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(
                    fieldName + " is required."
            );
        }

        if (value.contains("|")) {
            throw new ValidationException(
                    fieldName + " must not contain the '|' character."
            );
        }

        return value.trim();
    }

    public static int parseInt(
            String value, String fieldName
    ) throws ValidationException {

        String text = requireNotEmpty(value, fieldName);

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    fieldName + " must be a valid integer."
            );
        }
    }

    public static int parsePositiveInt(
            String value, String fieldName
    ) throws ValidationException {

        int number = parseInt(value, fieldName);

        if (number <= 0) {
            throw new ValidationException(
                    fieldName + " must be a positive integer."
            );
        }

        return number;
    }

    public static double parseNonNegativeDouble(
            String value, String fieldName
    ) throws ValidationException {

        String text = requireNotEmpty(value, fieldName);

        double number;

        try {
            number = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a valid number."
            );
        }
        if (Double.isNaN(number) || number < 0)

        return number;
    }
}