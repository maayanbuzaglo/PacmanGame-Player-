
This project is a kind of a pacman game, and like any pacman game, fruits are eaten by the pacmans. But this game is about our player. He needs to eat all the fruits at the best time and to get the best score he can. He can also eat pacmans (and get more score for it), but there are ghosts that run after him and while they touch him he looses score. Beyond that, there are black boxes where the player can not walk (pacmans and ghosts can), once he reaches the borders of the boxes he's stuck in place. As long as he's stuck, he looses score.

What distinguishes this game is that the game background is a map that represents a real map which you can find on https://www.google.com/earth/. \
***Learn more about Global's coordinate system here: https://en.wikipedia.org/wiki/Geographic_coordinate_system.***

(The game background map)
![alt text](https://github.com/maayanbuzaglo/OopNavigtion/blob/master/pictures/Ariel1.png)

The pacmans, the fruits, the ghosts and the player in the game represent real coordinates on Earth which are the real location on the map in the background.

You decide where the player would be located by a mouse click on the map after reading a game from a csv file, which already have all the data of the other characters.

There are two options to play the game:
1. A control run option - it means you control the player, where will he go and what fruit or pacman should he eat next.
2. An automatic run option - an algorithm that computes where will the player go and what fruit should he eat next considered the black boxes.

### The purpose of the game is to make the player eat all the fruits on the map in the shortest time possible and the best score.

The game ends if all the fruits were eaten, or, the time is over.

We will talk about the main classes.
## Map:
A class that represents a map that contains a map image file and all the necessary parameters of its alignment to a global coordinate system. The class enables conversion of global representation coordinates to the pixel in the image and vice versa. It also has a function which changes the coordinates according to the screen size.

## Pixel:
A class that represents a pixel in the game.

## Algorithm:
A class that gets game and receives the optimal path (shortest) so that all fruits will be "eaten" as quickly as possible considered the black boxes the player can not walk in. This is the main algorithmic class and includes calculating "fruit tracks" for the player. The purpose of the algorithm is to minimize the amount of time it takes for the player to eat all the fruits.\
The algorithm works as long as there are fruits on the map. It checks what fruit is the closest to the player and computes the smallest path to it. Then, removes the eaten fruit, move the player's coordinate to the coordinate of the eaten fruit, and repeat the algorithm.

## MyFrame:
A class that represents the game frame.
A graphical class that allows robots and fruits to be displayed on the map, displaying the activity of algorithms and performing a reconstruction of data from csv files.




# Hope you'll enjoy :+1:
