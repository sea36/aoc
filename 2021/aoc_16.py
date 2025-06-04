HEX_BITS = {
    '0': [0, 0, 0, 0],
    '1': [0, 0, 0, 1],
    '2': [0, 0, 1, 0],
    '3': [0, 0, 1, 1],
    '4': [0, 1, 0, 0],
    '5': [0, 1, 0, 1],
    '6': [0, 1, 1, 0],
    '7': [0, 1, 1, 1],
    '8': [1, 0, 0, 0],
    '9': [1, 0, 0, 1],
    'A': [1, 0, 1, 0],
    'B': [1, 0, 1, 1],
    'C': [1, 1, 0, 0],
    'D': [1, 1, 0, 1],
    'E': [1, 1, 1, 0],
    'F': [1, 1, 1, 1],
}

def hex_to_bin(hex_input):
    binary_output = []
    for ch in hex_input.strip():
        binary_output.extend(HEX_BITS[ch])
    return binary_output

def bin_to_dec(bits):
    return int("".join(str(x) for x in bits), 2)


def calc_packet_result(type, values):
    if type == 0:
        return sum(values)
    if type == 1:
        ret = 1
        for val in values:
            ret *= val
        return ret
    if type == 2:
        return min(values)
    if type == 3:
        return max(values)
    if type == 5:
        return 1 if values[0] > values[1] else 0
    if type == 6:
        return 1 if values[0] < values[1] else 0
    if type == 7:
        return 1 if values[0] == values[1] else 0


class Decoder:

    def decode(self, bits):
        self.bits = bits
        self.index = 0
        self.version_sum = 0
        values = []

        while len(bits) - (self.index + 1) > 6:
            values.append(self.parse_packet())

        return values

    def parse_packet(self):

        ix = self.index
        ix3 = ix + 3
        ix6 = ix + 6
        self.index += 6

        version = self.read_value(ix, ix3)
        type = self.read_value(ix3, ix6)

        self.version_sum += version
        if type == 4:
            return self.parse_literal()
        else:
            return self.parse_operator(type)

    def read_value(self, ix0, ix1):
        bits = self.bits[ix0:ix1]
        return bin_to_dec(bits)

    def parse_literal(self):
        bits = []
        while self.bits[self.index] == 1:
            bits.extend(self.bits[self.index+1:self.index+5])
            self.index += 5
        bits.extend(self.bits[self.index+1:self.index+5])
        self.index += 5

        return bin_to_dec(bits)


    def parse_operator(self, type):
        length_type_id = self.bits[self.index]
        self.index += 1

        values = []

        if length_type_id == 0:
            sub_packet_length = self.read_value(self.index, self.index + 15)
            self.index += 15

            end = self.index + sub_packet_length - 1
            while self.index < end:
                values.append(self.parse_packet())
        else:
            sub_packet_count = self.read_value(self.index, self.index + 11)
            self.index += 11

            for i in range(0, sub_packet_count):
                values.append(self.parse_packet())

        return calc_packet_result(type, values)

def part_a(hex):
    bin = hex_to_bin(hex)
    decoder = Decoder()
    decoder.decode(bin)
    return decoder.version_sum

def part_b(hex):
    bin = hex_to_bin(hex)
    decoder = Decoder()
    return decoder.decode(bin)