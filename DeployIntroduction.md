# Introduction #

Introduce how to deploy this project.


# Details #
## Download ##
All source code are available in SVN repository of this project. The root folder of source is in trunk/BirtChartJsExt, which is an eclipse Java project. You can use eclipse SVN plugin or other tools to download it. Regarding the download details, please follow the steps in checkout page.
## Output ##
After you get the source, you can use Ant to output the web archive jar(war). The build script is in root folder of project and the default target is to export the war. You must make sure JAVA\_HOME AND CATALINA\_HOME are set as environment variables correctly before running the ant target.
## Deploy ##
After the war is outputted successfully, you can deploy it by just putting it under Tomcat's webapps folder. For WebLogic or WebSphere, please follow their user interfaces.