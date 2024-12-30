import 'package:flutter/material.dart';

class PopUp{
  static showPopup(BuildContext context,String message, {required bool isError}) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(isError ? "Error" : "Success"),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text("OK"),
            ),
          ],
        );
      },
    );
  }
}