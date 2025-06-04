
def get_image_bits(image):
    ymax = len(image)
    xmax = len(image[0])

    image_bits = set()
    for j in range(0, ymax):
        for i in range(0, xmax):
            if image[j][i] == '#':
                image_bits.add((i, j))

    return image_bits

def is_lit(x, y, image_bits, algorithm):
    bits = []
    for j in range(-1, 2):
        for i in range(-1, 2):
            p = (x + i, y + j)
            bits.append('1' if p in image_bits else '0')
    pixel_index = int("".join(bits), 2)
    return algorithm[pixel_index] == '#'

def calculate_infinity(infinity, algorithm):
    pixel_index = 0 if infinity == '.' else 511
    return algorithm[pixel_index]

def create_image(image_bits):

    xmin = min([x for (x, y) in image_bits])
    ymin = min([y for (x, y) in image_bits])
    xmax = max([x for (x, y) in image_bits])
    ymax = max([y for (x, y) in image_bits])

    image = [["." for j in range(xmin, xmax+1)] for i in range(ymin, ymax+1)]
    for p in image_bits:
        image[p[1] - ymin][p[0] - xmin] = '#'
    return image


def populate_infinity(image, image_bits):
    ymax = len(image)
    xmax = len(image[0])

    for j in range(-3, 0):
        for i in range(-3, xmax + 3):
            image_bits.add((i,j))
    for j in range(0, ymax):
        for i in range(-3, 0):
            image_bits.add((i,j))
    for j in range(0, ymax):
        for i in range(xmax, xmax + 3):
            image_bits.add((i,j))
    for j in range(xmax, xmax + 3):
        for i in range(-3, xmax + 3):
            image_bits.add((i,j))


def enhance_image(image, algorithm, infinty_value):

    image_bits = get_image_bits(image)

    xmin = -2
    ymin = -2
    xmax = max([x for (x, y) in image_bits]) + 2
    ymax = max([y for (x, y) in image_bits]) + 2

    if infinty_value == '#':
        populate_infinity(image, image_bits)    

    new_image_bits = set()
    for x in range(xmin, xmax + 1):
        for y in range(ymin, ymax + 1):
            if is_lit(x, y, image_bits, algorithm):
                new_image_bits.add((x, y))
                
    return create_image(new_image_bits)


def enhance(algo, image, steps):
    infinity = "."
    for i in range(0, steps):
        image = enhance_image(image, algo, infinity)
        infinity = calculate_infinity(infinity, algo)
    return len(get_image_bits(image))

