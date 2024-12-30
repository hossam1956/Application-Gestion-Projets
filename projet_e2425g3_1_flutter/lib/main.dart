import 'package:flutter/material.dart';
import 'package:projet_e2425g3_1_flutter/presentation/pages/dashboard.dart';
import 'package:projet_e2425g3_1_flutter/presentation/pages/login.dart';
import 'package:projet_e2425g3_1_flutter/presentation/pages/signUp.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return  MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: "/",
      routes: {
        '/':(context)=> const Login(),
        '/signup':(context)=> const SignUp(),
        '/dashboard':(context)=> const Dashboard(),
      },
    );
  }
}

 