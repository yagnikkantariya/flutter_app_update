library app_update_popup;

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class FlutterAppUpdate {
  final platform =
      const MethodChannel('flutterUpdateChecker/isUpdateAvailable');
  initMethod({String? appId}) async {
    final bool result = await platform.invokeMethod(
        'setApplicationID', {"AppId": "157a00b3-6f60-44ba-bb44-0de60df650dc"});
    if (result) {
        await _check();
    }
  }

  Future<void> _check() async {
    String updateCheck = '';
    try {
      final bool result = await platform.invokeMethod(
        'isUpdateAvailable',
      );
      updateCheck = result ? "Update is available" : "Update is not available";
    } on PlatformException catch (e) {
      updateCheck = "Failed to check for update: '${e.message}'.";
    }
    if (kDebugMode) {
      print(updateCheck);
    }
  }
}
