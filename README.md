README
=============

You can put this tool to your Android Project folder to deploy app. it can help you build two the same Android apps with different package name.

Download
------
[the link](https://dl.dropboxusercontent.com/u/1161093/PackageApp.jar)

First
-------
	java -jar PackageApp.jar [-e] -p {ProjectName} [-d {DebugPackageName}]
* -e: whether generate External Tools
* -p: the project name
* -d: the development package name

Second
-------
	ant debugapp
	ant releaseapp

References
-------
* [使用ANT打包Android应用](http://blog.csdn.net/liuhe688/article/details/6679879)
* [在 Eclipse 內，用 Ant 編譯你的 Android 程式](http://ysl-paradise.blogspot.com/2008/09/eclipse-ant-android.html)
* [Building Two Versions of the Same Android App](http://blog.uncommons.org/2010/07/19/building-two-versions-of-the-same-android-app/)
* [Custom Android build.xml for rename manifest package](http://stackoverflow.com/questions/3360349/custom-android-build-xml-for-rename-manifest-package)
* [Using Ant to Automate Building Android Applications](http://www.androidengineer.com/2010/06/using-ant-to-automate-building-android.html)
* [Managing Projects from the Command Line](http://developer.android.com/guide/developing/projects/projects-cmdline.html)
* [Building and Running from the Command Line](http://developer.android.com/guide/developing/building/building-cmdline.html#ReleaseMode)
* [Help - Eclipse Platform: Workbench User Guide > Concepts > Ant & External tools](http://help.eclipse.org/helios/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Fconcepts%2Fconcepts-exttools.htm)
* [Exporting (and importing) configured External Tools](http://www.eclipse.org/forums/index.php/m/253635/)
* [MyEclipse: Way to export external tools?](http://www.myeclipseide.com/PNphpBB2-printview-t-11970-start-0.html)
* [How to install ant 1.8.2 using PPA on ubuntu](http://www.ubuntugeek.com/how-to-install-ant-1-8-2-using-ppa-on-ubuntu.html)
