import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:projet_e2425g3_1_flutter/model/project.dart';
import 'package:projet_e2425g3_1_flutter/services/projectsUserService.dart';

class ProjectList extends StatefulWidget {
  final int userId;
  const ProjectList({super.key, required this.userId});

  @override
  State<ProjectList> createState() => _ProjectListState();
}

class _ProjectListState extends State<ProjectList> {
  late Future<List<Project>> _futureProjectsUser;
  late List<bool> showableStates = [];
  @override
  void initState() {
    super.initState();
    _futureProjectsUser =
        ProjectsUserService().fetchProjectOfUser(widget.userId);
  }

  void toggleShowable(int index) {
    setState(() {
      showableStates[index] = !showableStates[index];
    });
  }

  @override
  Widget build(BuildContext context) {
    final double iconSize = MediaQuery.of(context).size.width * 0.06;
    return FutureBuilder<List<Project>>(
        future: _futureProjectsUser,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData && snapshot.data!.isNotEmpty) {
            final List<Project> projects = snapshot.data!;
            if (showableStates.length != projects.length) {
              showableStates = List<bool>.filled(projects.length, false);
            }
            return SizedBox(
              height: MediaQuery.of(context).size.height * 0.5,
              child: ListView.builder(
                  itemCount: projects.length,
                  itemBuilder: (context, index) {
                    final project = projects[index];
                    return Column(children: [
                      ListTile(
                          leading: const Icon(Icons.folder, color: Colors.grey),
                          title: Text(project.nom),
                          subtitle: Text(
                              'Start: ${project.datedebut.toIso8601String().split('T')[0]}\nEnd: ${project.datefin.toIso8601String().split('T')[0]}'),
                          trailing: InkWell(
                            onTap: () => toggleShowable(index),
                            child: Icon(
                              !showableStates[index]
                                  ? Icons.arrow_drop_down
                                  : Icons.arrow_drop_up,
                              color: Colors.grey,
                            ),
                          )),
                      if (showableStates[index])
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 16.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              ListTile(
                                leading: SvgPicture.asset(
                                  'lib/assets/icons/team-fill-svgrepo-com.svg',
                                  width: iconSize,
                                  height: iconSize,
                                ),
                                title: const Text('Members'),
                              ),
                              ListTile(
                                leading: SvgPicture.asset(
                                  'lib/assets/icons/squares-four-thin-svgrepo-com.svg',
                                  width: iconSize,
                                  height: iconSize,
                                ),
                                title: const Text('Task Board'),
                              ),
                              ListTile(
                                leading: SvgPicture.asset(
                                  'lib/assets/icons/list-unordered-svgrepo-com.svg',
                                  width: iconSize,
                                  height: iconSize,
                                ),
                                title: const Text('Task Lists'),
                              ),
                              ListTile(
                                leading: SvgPicture.asset(
                                  'lib/assets/icons/calender-svgrepo-com.svg',
                                  width: iconSize,
                                  height: iconSize,
                                ),
                                title: const Text('Calender'),
                              ),
                              ListTile(
                                leading: SvgPicture.asset(
                                  'lib/assets/icons/chat-round-dots-svgrepo-com.svg',
                                  width: iconSize,
                                  height: iconSize,
                                ),
                                title: const Text('Chat'),
                              ),
                              const SizedBox(height: 10),
                            ],
                          ),
                        ),
                    ]);
                  }),
            );
          } else {
            return const Center(child: Text('No projects found'));
          }
        });
  }
}
