import 'package:flutter/material.dart';

class CreateProject extends StatefulWidget {
  const CreateProject({super.key});

  @override
  State<CreateProject> createState() => _AddProjectState();
}
void _submitProject(BuildContext context) async {}

class _AddProjectState extends State<CreateProject> {
  @override
  Widget build(BuildContext context) {
    return Dialog(
      child: Padding(
      padding:const EdgeInsets.all(8.0),
      child: Form(child: Column(
        children: [
          TextFormField(
            decoration:const InputDecoration(
              icon: Icon(Icons.folder),
              labelText:'Project Name' 
            ),
            validator: (value){
              if (value == null || value.isEmpty) {
                    return 'Please enter the project name';
                  }
                  return null;
            },
          ),
           const SizedBox(height: 20),
            ElevatedButton(
                onPressed: () => _submitProject(context),
                child: const Text('Submit'),
              )
        ],
      )),
    ),
    ); 
    
    
  }
}