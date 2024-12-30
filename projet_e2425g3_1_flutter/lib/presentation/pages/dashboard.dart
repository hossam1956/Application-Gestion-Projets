import 'package:flutter/material.dart';
import 'package:projet_e2425g3_1_flutter/presentation/widgets/appDrawer.dart';
import 'package:projet_e2425g3_1_flutter/presentation/widgets/buttonDrawer.dart';

class Dashboard extends StatelessWidget {
  const Dashboard({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: appBar(),
      backgroundColor: Colors.white,
      drawer: AppDrawer(),
    );
  }

  AppBar appBar() {
    return AppBar(
      title: const Text(
        "Dashboard",
        style: TextStyle(
            color: Colors.black, fontSize: 25, fontWeight: FontWeight.bold),
      ),
      backgroundColor: Colors.white,
      elevation: 0.0,
      centerTitle: true,
      leading: Builder(builder: (context) {
        return const ButtonDrawer();
      }),
      actions: [
        CircleAvatar(
          radius: 50.0,
          backgroundImage: const NetworkImage(
              'https://gestprojbucket.s3.us-east-1.amazonaws.com/user-profile-images/09948cb9-85e1-49a6-9afb-e115a25cbadb-download.jpg'),
          onBackgroundImageError: (exception, stackTrace) => {},
        )
      ],
    );
  }
}
