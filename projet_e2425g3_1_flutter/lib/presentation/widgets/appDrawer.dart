import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:projet_e2425g3_1_flutter/presentation/widgets/forms/createProject.dart';
import 'package:projet_e2425g3_1_flutter/presentation/widgets/projectList.dart';

class AppDrawer extends StatelessWidget {
  const AppDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: Colors.white,
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          const DrawerHeader(
              child: Text(
            'Project Management',
            style: TextStyle(fontSize: 35, fontWeight: FontWeight.bold),
          )),
          InkWell(
            onTap: () {
              print("Tapped on Dashboard!");
            },
            borderRadius: BorderRadius.circular(8.0),
            splashColor: const Color(0xFF696cff),
            highlightColor: Colors.transparent,
            child: ListTile(
              leading: SvgPicture.asset('lib/assets/icons/home-svgrepo-com.svg',
                  width: MediaQuery.of(context).size.width * 0.06,
                  fit: BoxFit.contain),
              title: const Text('Dashboard', style: TextStyle(fontSize: 25)),
            ),
          ),
          ListTile(
            title: const Text('My Projects : ',
                style: TextStyle(
                    fontSize: 20,
                    color: Color.fromARGB(255, 117, 117, 117),
                    fontWeight: FontWeight.bold)),
            trailing: InkWell(
              onTap: () {
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (context) => const CreateProject(),
                  ),
                );
              },
              borderRadius: BorderRadius.circular(8.0),
              splashColor: const Color(0xFF696cff),
              highlightColor: Colors.transparent,
              child: SvgPicture.asset('lib/assets/icons/add-to-svgrepo-com.svg',
                  width: MediaQuery.of(context).size.width * 0.06),
            ),
          ),
          const ProjectList(userId: 1)
        ],
      ),
    );
  }
}
