# COMP 4521 course project @ HKUST
UST as UST ­- U Share Them as UST Students!

Assignment graded - under mit license.
Seems I have included the reports in the 7z files... tell me if isn't.

Feel free continue this project (with different name maybe),
Tell me for requesting any support (but not working for your course project!),
Or just do it by yourself. Obviously there are rooms for improvement.
// - - - - - - - - -
Structure:
  As stated in the docs, Android client side, NodeJS server, JSON data formatting.
  
How to open and view those s___:
  Use Android studio to open the root directory.
  Use 7-zip to extract other files, use text editor to open js files.
  Install NodeJS, shift+right click on server directory, open cmd at directory, node server.
  If the empty database is not work, use database-test (sure rename it).
  GCM part is simple. Google and see the instruction.
// - - - - - - - - -
Rooms for improvement:
  MongoDB commands! To give faith to the boss. (Server side)
  Mapping of IMG files is a bit too messy. See matchIMG function. (Server side)
  Insert package.json for better support of npm commands. (Server side)
  Use async task for loading CalEvents in ChatFragment. (Client side)
  Use Android's AsyncTask and HttpRequest instead of loopJ's external libraries. For style. (Client side)
  Since it moved from Ecplise to Android Studio, revise the structure, remove necessary codes, 
    also run a few more pass of code inspect. (Client side)
  Support more multimedia formats like Wha__App. (Client side)
  Continuous UI improvement please. (Client side)
  Renaming. Very important.
// - - - - - - - - -
History:
2014 Spring: 
  Jason's group created this for course project. 
  Named Ex-UST. 1 carry, 1 deadline fighter, 1 freerider.
  Created the basic structure of Android client - Course, Calendar, Chat.
  Created server.js and download.js.
  Thought to use MongoDB. Due to XXX issues, server handle the data instead.
  Used SVN but you know, Google shut it down.
2015 Spring: 
  Darren's group continued this for course project (same course). 
  Named UasT. 1 carry, 1 deadline fighter, 1 freerider.
  Added image supporting (JPG only, hard-coded) for all components.
  Filled the huge hole of Calendar function.
  Made the js files supporting the updated external libraries (as in 2015).
  Didn't think for using MongoDB. But the review reminded me a lot.
// - - - - - - - - -
Copyright (C) 2015 Darren Lau, Hugo Cheung, Starry Ho, Jason Chan, and those who worked before

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
