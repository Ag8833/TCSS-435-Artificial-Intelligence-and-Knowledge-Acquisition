'''
    File name: PA3.py
    Author: Andrew Gates
    Date created: 6/5/2017
    Date last modified: 6/5/2017
    Python Version: 2.7
'''

import random

textLimit = 1000
triGramList = []
triGramOutput = open('TriGramList.txt', 'w')

# Class that contains a constructor for a triGram which includes the children associated with it, as well as the current word, 
# the current depth, and the current count. It also contains functions to update the current word if new words need to be 
# chained to it. As well as functions to print out all of the trigrams to the console or to a text file.
class triGram():
    def __init__(self, depth, currentGram):
        # List to be used for chidren of the current gram
        self.childrenList = []
        # Set the word to be equal to the 0 index of the current gram
        self.word = currentGram[0]
        # Set the depth to be equal to the depth sent in
        self.depth = depth
        # Set the count to 1 initially
        self.count = 1
        
        # If the currentGram has more words then add them to the children list
        if(len(currentGram) > 1):
            self.childrenList.append(triGram(depth + 1, currentGram[1:]))
            
    def wordUpdate(self, newGram):
        self.count += 1
        # Loop looking to see if self.childrenList contains newGram[1]
        for childOne in self.childrenList:
            #print(childOne.word)
            # If self.childrenList contains the newGram[1], update count and check second word
            if(newGram[1] == childOne.word):
                #print("CHILD ONE EQUAL")
                childOne.count += 1
                # Loop looking to see if childOne.childrenList contains newGram[2]
                for childTwo in childOne.childrenList:
                    #  If the first word's childrenList contains newGram[2] then update count and return
                    if(newGram[2] == childTwo.word):
                        #print("CHILD TWO EQUAL")
                        childTwo.count += 1
                        return
                # If childOne.childrenList did not contain newGram[2] then add it to it and return
                childOne.childrenList.append(triGram(2, newGram[2:]))
                return
        # if self.childrenList did not contain newGram[1] then add it to it and return
        self.childrenList.append(triGram(1, newGram[1:]))       
        return
    
    # Print all of the trigrams associated with the word to the console
    def printTriGrams(self):
        for childOne in self.childrenList:
            for childTwo in childOne.childrenList:
                print(self.word,childOne.word,childTwo.word)
          
    # Print all of the trigrams associated with the word to the TriGramList.txt file      
    def printTextTriGrams(self):
        for childOne in self.childrenList:
            for childTwo in childOne.childrenList:
                triGramOutput.write('1:')
                triGramOutput.write(self.word)
                triGramOutput.write(' 2:')
                triGramOutput.write(childOne.word)
                triGramOutput.write(' 3:')
                triGramOutput.write(childTwo.word)
                triGramOutput.write('\n')

# Function that will take the given text files and generate trigrams off of every set of 3 words in each text. It will then 
# check to see if a trigram has been previously defined with the same first word, if it has it will add the new trigram onto
# the previous one. If the first word has not been previously defined it will creat a new index for the current trigram. 
def textProcess():
    currentList = []
    textList = []
    wordExists = False
    
    textList.append('alice-27.txt') 
    textList.append('doyle-27.txt')
    textList.append('doyle-case-27.txt')
    textList.append('london-call-27.txt')
    textList.append('melville-billy-27.txt')
    textList.append('twain-adventures-27.txt')

    # Loop through each text that is being used
    for text in textList:
        print(text)
        # Open current text as currentFile
        with open(text,"r") as currentFile:
            # Loop through each line in the current text
            for currentLine in currentFile:
                # Loop through each word in the current line
                for currentWord in currentLine.split():
                    # Add the current word to current list
                    currentList.append(currentWord.lower())
                    #print(currentList)
                    # If there are currently 3 items in the current list
                    if(len(currentList) == 3):
                        # Loop through each word in the list of trigrams
                        for currentTriGram in triGramList:
                            # If the current trigram starts with the same word as the start of current list, update that trigram word
                            if(currentTriGram.word == currentList[0]):
                                currentTriGram.wordUpdate(currentList)
                                wordExists = True
                                break
                        # If the current trigram did not start with the same word as the start of current list, add that new trigram
                        if(wordExists == False):
                            triGramList.append(triGram(0, currentList))
                        # Remove the first element to allow for a new element to be added to the end to make a new trigram
                        currentList.remove(currentList[0])
                        wordExists = False
        # Reset list so words don't carry between texts
        currentList = []
              
# Function that calls the printTriGrams and printTextTriGrams functions to check the trigrams being created
def printGrams():
    for word in triGramList:
        word.printTriGrams()
        word.printTextTriGrams()
    print(len(triGramList) - 1)
        
# Function that takes the triGramList and first creates a chain of words with the first being random, the second being random, and the 
# third being the word with the highest frequency. Then it will use the previous third word as the new first one, and creates a chain 
# using the previous third word as the first word each time, while the second being random, and the third being the word with the highest 
# frequency. Once 1000 words are created it will write the createdText array to the output.txt file.
def createText():
    createdText = []
    bestFrequency = 0
    thirdWord = ""
    wordFound = False
    
    while(len(createdText) < textLimit):
        # If there are no words in the text, generate a first word
        if(len(createdText) == 0):
            # Find the first word randomly, words that appear more often will have a higher chance to be picked
            firstIndex = random.randint(0, len(triGramList) - 1)
            firstWord = triGramList[firstIndex]
            # Append the first word to the text, after that only append two words at a time for a proper chain
            createdText.append(firstWord.word)
            
        # If there area already words in the text, use the previous third word as the first
        else:
            wordFound = False
            # Loop through triGramList to check if the previous third word can be used as the first word
            for currentGram in triGramList:
                # If it can then set the firstWord to the current trigram
                if(currentGram.word == thirdWord.word):
                    wordFound = True
                    firstWord = currentGram
              
            # If the word was not found, generate a new first word and append that to the text
            if(wordFound == False):
                firstIndex = random.randint(0, len(triGramList) - 1)
                firstWord = triGramList[firstIndex]
                createdText.append(firstWord.word)
        
        #print("1: ", firstWord.word)
        
        # Generate a random second word based off of the fist word
        secondIndex = random.randint(0, len(firstWord.childrenList) - 1)
        secondWord = firstWord.childrenList[secondIndex]    
        #print("2: ", secondWord.word)
        
        # Clear the previous third word and frequency
        del(thirdWord)
        bestFrequency = 0
        
        # Loop through the words in the second word's children to find the word with the highest frequency
        for currentWord in secondWord.childrenList:
            # P(Wi,Wi+1,Wi+2)/P(Wi+1,Wi+2)
            currentFrequency = (currentWord.count)/(float(secondWord.count))
            # If the current word appears more often, replace it as the candidate for the third word
            if(currentFrequency > bestFrequency):
                thirdWord = currentWord
                bestFrequency = currentFrequency
        #print("3: ", thirdWord.word)
        
        # Append the second and third words that were chosen
        createdText.append(secondWord.word)
        createdText.append(thirdWord.word)
        
    # Write the createdText array into the output.txt file
    createdTextOutput = open('output.txt', 'w')
    for word in createdText:
        createdTextOutput.write(word)
        createdTextOutput.write(' ')
        
textProcess()
#printGrams()
createText()
