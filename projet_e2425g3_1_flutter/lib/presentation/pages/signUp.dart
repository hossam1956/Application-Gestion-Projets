import 'dart:io';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:projet_e2425g3_1_flutter/validators/popUp.dart';
import "package:projet_e2425g3_1_flutter/validators/validators.dart";
import 'package:http/http.dart' as http;

class SignUp extends StatefulWidget {
  const SignUp({super.key});

  @override
  State<SignUp> createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _confPasswordController = TextEditingController();
  File? _image;
  bool _isUsernameValid = true;
  bool _isEmailValid = true;
  bool _isPasswordValid = true;
  bool _doPasswordsMatch = true;
  bool _isLoading = false;

  Future<void> _pickImage() async {
    try {
      final picker = ImagePicker();
      final pickedFile = await picker.pickImage(source: ImageSource.gallery);

      if (pickedFile != null) {
        setState(() {
          _image = File(pickedFile.path);
        });
      }
    } catch (e) {
      print("Error picking image: $e");
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to pick image')),
      );
    }
  }

  bool _isValidImage(File? image) {
    if (image == null) return false;
    final mimeType = image.path.split('.').last.toLowerCase();
    return mimeType == 'jpeg' || mimeType == 'png';
  }

  Future<void> __submitData() async {
    final username = _usernameController.text.trim();
    final email = _emailController.text.trim();
    final password = _passwordController.text.trim();
    if (username.isEmpty ||
        email.isEmpty ||
        password.isEmpty ||
        _image == null ||
        !_isEmailValid ||
        !_isPasswordValid ||
        !_isUsernameValid ||
        !_doPasswordsMatch) {
      PopUp.showPopup(context, "All fields are required in the correct format",
          isError: true);
      return;
    }
      setState(() {
        _isLoading = true;
       });
    try {
      Uri url = Uri.parse("http://localhost:8082/api/user/create");
      final request = http.MultipartRequest('POST', url);
      request.fields['username'] = username;
      request.fields['email'] = email;
      request.fields['password'] = password;
      request.files
          .add(await http.MultipartFile.fromPath('profileImage', _image!.path));
      final response = await request.send();
      if (response.statusCode == 201) {
        setState(() {
          _isLoading = false;
        });
        PopUp.showPopup(context, "User created successfully", isError: false);
        Future.delayed(Duration(seconds: 2), () {
          Navigator.pushReplacementNamed(context, '/');
        });
      } else if (response.statusCode == 400) {
        setState(() {
          _isLoading = false;
        });
        final responseBody = await response.stream.bytesToString();
        PopUp.showPopup(context, responseBody, isError: true);
      } else {
        setState(() {
          _isLoading = false;
        });
        PopUp.showPopup(context, "Error signing up", isError: true);
      }
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      PopUp.showPopup(context, "Error signing up", isError: true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Sign Up"),
        backgroundColor: Colors.white,
      ),
      backgroundColor: Colors.white,
      body: Stack(
        children: [
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 0.0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text(
                  "Please Enter your informations",
                  style: TextStyle(fontSize: 23, fontWeight: FontWeight.bold),
                ),
                SizedBox(height: MediaQuery.of(context).size.height * 0.1),
                TextField(
                  controller: _usernameController,
                  decoration: InputDecoration(
                      labelText: 'Username',
                      prefixIcon: const Icon(Icons.person),
                      border: const OutlineInputBorder(),
                      errorText: _isUsernameValid
                          ? null
                          : "Username Invalid.At least 4 caracters"),
                  onChanged: (value) {
                    setState(() {
                      if (value.isNotEmpty) {
                        _isUsernameValid = Validators.isValidUsername(value);
                      } else {
                        _isUsernameValid = true;
                      }
                    });
                  },
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: _emailController,
                  decoration: InputDecoration(
                      labelText: 'Email',
                      prefixIcon: const Icon(Icons.mail),
                      border: const OutlineInputBorder(),
                      errorText: _isEmailValid ? null : "Email invalid"),
                  onChanged: (value) {
                    setState(() {
                      if (value.isNotEmpty) {
                        _isEmailValid = Validators.isValidEmail(value);
                      } else {
                        _isEmailValid = true;
                      }
                    });
                  },
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: _passwordController,
                  decoration: InputDecoration(
                    labelText: 'Password',
                    prefixIcon: const Icon(Icons.lock),
                    border: const OutlineInputBorder(),
                    errorText: _isPasswordValid
                        ? null
                        : "Password is invalid.At least 8 caracters and 1 digit",
                  ),
                  onChanged: (value) {
                    setState(() {
                      if (value.isNotEmpty) {
                        _isPasswordValid = Validators.isValidPassword(value);
                      } else {
                        _isPasswordValid = true;
                      }
                    });
                  },
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: _confPasswordController,
                  decoration: InputDecoration(
                      labelText: 'Confirm your Password',
                      prefixIcon: const Icon(Icons.lock),
                      border: const OutlineInputBorder(),
                      errorText:
                          _doPasswordsMatch ? null : "Password doesn't match"),
                  onChanged: (value) {
                    setState(() {
                      if (value.isNotEmpty) {
                        _doPasswordsMatch = Validators.isPasswordsMatchs(
                            _passwordController.text, value);
                      } else {
                        _doPasswordsMatch = true;
                      }
                    });
                  },
                ),
                const SizedBox(height: 16),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    CircleAvatar(
                      radius: 50,
                      backgroundColor: const Color.fromARGB(255, 228, 225, 233),
                      backgroundImage: _image != null
                          ? FileImage(_image!)
                          : const AssetImage(
                              'lib/assets/images/placeholder.png'),
                    )
                  ],
                ),
                Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                  ElevatedButton(
                    style: ButtonStyle(
                      backgroundColor: WidgetStateProperty.resolveWith<Color?>(
                        (Set<WidgetState> states) {
                          if (states.contains(WidgetState.pressed)) {
                            return Theme.of(context)
                                .colorScheme
                                .primary
                                .withOpacity(0.5);
                          }
                          return null;
                        },
                      ),
                    ),
                    child: const Text(
                      'Importer',
                      style: TextStyle(fontSize: 10),
                    ),
                    onPressed: () {
                      _pickImage();
                    },
                  ),
                ]),
                const SizedBox(height: 16),
                ElevatedButton(
                    onPressed: __submitData,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.deepPurple,
                      foregroundColor: Colors.white,
                      shadowColor: Colors.purpleAccent,
                      elevation: 8,
                    ),
                    child: const Text("Sign Up")),
                const SizedBox(height: 16),
                InkWell(
                  onTap: () {
                    Navigator.pushNamed(context, '/');
                  },
                  child: const Text(
                    "You are already a member,Sign Up",
                    style: TextStyle(color: Colors.deepPurple),
                  ),
                )
              ],
            ),
          ),
          if (_isLoading) _buildBlurOverlay(context),
        ],
      ),
    );
  }

  Widget _buildBlurOverlay(BuildContext context) {
    return BackdropFilter(
      filter: ImageFilter.blur(sigmaX: 5, sigmaY: 5),
      child: Container(
        color: Colors.black.withOpacity(0.5), 
        child: const Center(
          child: CircularProgressIndicator(
            color: Colors.white, 
          ),
        ),
      ),
    );
  }
}
