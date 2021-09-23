# LZW-Compression
From a functionality perspective: 
The encode method has been improved from prior versions although it is still not ideal -- before the encode method did not encode the current value and instead
added the dictionary value (256,257 . . . ) sequentially. Now it at least correctly encodes the current value (edge cases have not been tested).
The decode method appears to be functioning correctly. 

From a big O perspective:
A for loop has been eliminated in the encdoe method which caused a decrease in run time from 11587 ---> 45 overall. 
Some of this decrease can be attributed to eliminating System.out.println methods that were used by the initial coder to test the code
System.out.println have also been removed from the decode method decreasing the runtime from 4298 --> 14 overalll

From a code clarity persepctive: at least 1 method was renamed to more accurately reflect what the method accomplishes. 
I tried very hard to maintain Louis and Tyler's stylistic variable names and method names --- comments were used to explain methods that were oddly named 
or carried out numerous operations at the same time. 
