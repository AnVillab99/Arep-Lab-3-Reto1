# Arep-Lab-3
[![CircleCI](https://circleci.com/gh/AnVillab99/Arep-Lab-3-Reto1.svg?style=svg)](https://circleci.com/gh/AnVillab99/Arep-Lab-3-Reto1)

This laboratory intends for the students to create web servers on java, that are able to recieve get petitions and asnwer them with the requested file, if it exists.

## Getting Started

You can download a copy of the project from github.

### Prerequisites

You will need maven, java and heroku cli installed on the computer to run this program.


### Installing

First you need to install java and jdk.
Go to java page and download both, install them and add the paths of those folders to the enviroment variables of your pc.
For maven you download the zip archive from the maven page and unpack it on a specific folder, the you add the path to that folder on the enviroment variables
For Heroku, you need to go to heroku main page and follow thye download and installation instructions, after that you need to setup the heroku credentials.
To deploy on local, the Procfile must be changed to:
>web: java -cp target\classes;target\dependency\* edu.escuelaing.arep.designprimer.SparkWebApp


## Running the program

To run this program on local, write in console:
> heroku local web (port:5000)
or run directly the java code of webServer (port:4567)

the existing files are : 
                /index.html
                /img1.jpg
                /img2.png

index page  :
![index.html](https://github.com/AnVillab99/Arep-Lab-3-Reto1/blob/master/resources/img/index.PNG)

Not found page :
![not found](https://github.com/AnVillab99/Arep-Lab-3-Reto1/blob/master/resources/img/notFound.PNG)

Not supported media page :
![not supportde media](https://github.com/AnVillab99/Arep-Lab-3-Reto1/blob/master/resources/img/notSupportedMedia.PNG)

img1.jpg:
![jpg](https://github.com/AnVillab99/Arep-Lab-3-Reto1/blob/master/resources/img/img1.PNG)

img2.png :
![png](https://github.com/AnVillab99/Arep-Lab-3-Reto1/blob/master/resources/img/img2.PNG)


## Design

On the emebeded pdf document is a simple description of the design of this project 


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [JAVA](https://www.java.com/es/download) - Framework
* [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Framework
* [Heroku](https://devcenter.heroku.com/articles/heroku-cli) - Deployment

## Versioning

For the versions available, see the [tags on this repository](https://github.com/AnVillab99/AREP-Lab1/tags). 

## Authors

* **Andres Villamil**  [AnVillab99](https://github.com/AnVillab99)


## License

This project is under GNU General Public License - see [LICENSE](https://github.com/AnVillab99/AREP-Lab1/blob/master/LICENSE) to more info.

## Acknowledgments

* StackOverflow

