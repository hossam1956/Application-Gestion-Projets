import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';

class ButtonDrawer extends StatefulWidget {
  const ButtonDrawer({super.key});

  @override
  State<ButtonDrawer> createState() => _ButtondrawerState();
}

class _ButtondrawerState extends State<ButtonDrawer> {
  Color _buttonColor = Colors.white;
  @override
  Widget build(BuildContext context) {
    return InkWell(
       onTap: () {
        Scaffold.of(context).openDrawer();
      },
       onTapDown: (_) {
        setState(() {
          _buttonColor = const Color(0xFF696cff); 
        });
      },
       onTapUp: (_) {
        setState(() {
          _buttonColor = Colors.white;
        });
      },
      onTapCancel: () {
        setState(() {
          _buttonColor = Colors.white; 
        });
      },
      splashColor: Colors.transparent,
      highlightColor: Colors.transparent,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        margin: const EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: _buttonColor,
          borderRadius: BorderRadius.circular(10.0),
        ),
        child: SvgPicture.asset(
          'lib/assets/icons/right-arrow-svgrepo-com.svg',
        ),
      ),

    );
    
  }
}