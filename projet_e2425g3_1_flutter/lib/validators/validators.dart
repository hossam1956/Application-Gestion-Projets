class Validators {

    static const String usernameRegex = r'^[a-zA-Z0-9]{4,10}$';
    static const String emailRegex = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$';
    
    // Regex for password validation (at least 8 characters, 1 letter, 1 number)
    static const String passwordRegex = r'^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$';

     static bool isValidUsername(String username) {
      return RegExp(usernameRegex).hasMatch(username);
    }

    static bool isValidEmail(String email) {
      return RegExp(emailRegex).hasMatch(email);
    }

    static bool isValidPassword(String password) {
      return RegExp(passwordRegex).hasMatch(password);
    }
    static bool isPasswordsMatchs(String password,String confPassword) {
      return password == confPassword;
    }

}