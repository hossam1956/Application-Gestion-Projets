import 'package:flutter/material.dart';
import 'package:projet_e2425g3_1_flutter/presentation/pages/dashboard.dart';

class Login extends StatefulWidget {
  const Login({super.key});

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool obscureTextPasswd = true;


  void _login() {
    final email = _emailController.text;
    final password = _passwordController.text;
    if (email == 'test@example.com' && password == 'password') {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const Dashboard()),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Invalid credentials!')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Sign In"),backgroundColor: Colors.white,),
      backgroundColor: Colors.white,
      body: Padding(
        padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 0.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text("Please Enter your informations",style: TextStyle(fontSize: 23,fontWeight: FontWeight.bold),),
            SizedBox(height: MediaQuery.of(context).size.height * 0.1),
            TextField(
              controller: _emailController,
              decoration: const InputDecoration(
                labelText: 'Email',
                prefixIcon: Icon(Icons.mail),
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _passwordController,
              decoration: InputDecoration(
                labelText: 'Password',
                prefixIcon: const Icon(Icons.lock),
                suffixIcon: InkWell(
                  onTap: () {
                    setState(() {
                      obscureTextPasswd = !obscureTextPasswd;
                    });
                  },
                  child:obscureTextPasswd?const Icon(Icons.visibility):const Icon(Icons.visibility_off) ,
                  
                ),
                border: OutlineInputBorder(),
              ),
              obscureText: obscureTextPasswd,
            ),
             const SizedBox(height: 16),
            ElevatedButton(
             onPressed:(){},
             style:ElevatedButton.styleFrom(
              backgroundColor: Colors.deepPurple,
              foregroundColor: Colors.white,
              shadowColor: Colors.purpleAccent,
              elevation: 8,
             ) ,
             child: const Text("Sign In")
             ),
             const SizedBox(height: 16),
             InkWell(
              onTap: (){
                Navigator.pushNamed(context, '/signup');
              },
              child: const Text("Not a member,Sign up",style: TextStyle(color:Colors.deepPurple ),),
             )
          ],
        ),
      ),
    );
  }
}
