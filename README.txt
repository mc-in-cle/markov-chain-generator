=====README=====

~~~~~ABOUT~~~~~
MARKOV CHAIN GENERATOR
Author: Minor Cline
First release: November 2018
This project is licensed under Mozilla Public License Version 2.0.

~~~~~OVERVIEW~~~~~
MARKOV CHAIN GENERATOR is a command-line Java application that generates semi-random text based on source text from a file. The technique used is a Markov chain, which is a way of describing the probability of sequential events.


~~~~~HISTORY OF THE PROJECT~~~~~
This application is based on a project I originally did in Computer Science 151, taught by Prof. Benjamin Kuperman, at Oberlin College in 2012.
Since then I have re-written and refactored the project. The back end is now able to handle any data type, not just text. It takes as input an Iterator with generic type, making it highly flexible. Data is stored Markov chain model in a structure called a "trie" which I chose to implement myself.


~~~~~INPUTTING DATA~~~~~
The user will place one or more text files in the project's root directory. The application will prompt the user for file names.

The application will also prompt the user to enter an integer for "model order." In general, the higher the model order, the more coherent the output will be. High model orders will not work well when the amount of input text is small, because the output could end up mimicking the input too closely. Additionally, high model orders require more memory and processing time.
Recommended values of model order are between 4 and 12.


~~~~~~OUTPUT~~~~~
MARKOV CHAIN GENERATOR will print its output to the console.

