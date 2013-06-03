ImagedData
==========

Saves any given data as a png image. This is done by first creating a huffman tree with the data, then using
that tree to get the huffman encoding of the data as a String of bits. Those bits are then stored in a list
of pixels, which gets mapped to a square BufferedImage. Finally, that BufferedImage is compressed and saved as
a PNG image. The reverse process is done for decoding the data.
