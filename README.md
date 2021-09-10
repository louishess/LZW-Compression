# LZW-Compression
The algorithm is slightly broken but the handling of the dictionary into binary is optimized properly – 9 bits are stored by 9 bits, not 2 bytes (16 bits); the algorithm needs to be touched up to store traditional ascii characters before doing other things so look out for that. If you fix the algorithm, the conversion into binary will still function properly.

https://www.youtube.com/watch?v=iik25wqIuFo watch this for specific information – instructional video on how to make algorithm. I commented on the bottom to explain what was wrong with mine.
