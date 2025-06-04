import itertools

ROT_X = ((1, 0, 0), (0, 0, -1), (0, 1, 0))
ROT_Y = ((0, 0, -1), (0, 1, 0), (1, 0, 0))
ROT_Z = ((0, -1, 0), (1, 0, 0), (0, 0, 1))

def rotate(p, rot):
    px = p[0]*rot[0][0] + p[1]*rot[0][1] + p[2]*rot[0][2]
    py = p[0]*rot[1][0] + p[1]*rot[1][1] + p[2]*rot[1][2]
    pz = p[0]*rot[2][0] + p[1]*rot[2][1] + p[2]*rot[2][2]
    return (px, py, pz)

def mat_mult(a, b):
    return (
        (a[0][0]*b[0][0]+a[0][1]*b[1][0]+a[0][2]*b[2][0], a[0][0]*b[0][1]+a[0][1]*b[1][1]+a[0][2]*b[2][1], a[0][0]*b[0][2]+a[0][1]*b[1][2]+a[0][2]*b[2][2]),
        (a[1][0]*b[0][0]+a[1][1]*b[1][0]+a[1][2]*b[2][0], a[1][0]*b[0][1]+a[1][1]*b[1][1]+a[1][2]*b[2][1], a[1][0]*b[0][2]+a[1][1]*b[1][2]+a[1][2]*b[2][2]),
        (a[2][0]*b[0][0]+a[2][1]*b[1][0]+a[2][2]*b[2][0], a[2][0]*b[0][1]+a[2][1]*b[1][1]+a[2][2]*b[2][1], a[2][0]*b[0][2]+a[2][1]*b[1][2]+a[2][2]*b[2][2])
    )

def combine_rotations(m, nx, ny, nz):
    for i in range(0,nx):
        m = mat_mult(m, ROT_X)
    for i in range(0,ny):
        m = mat_mult(m, ROT_Y)
    for i in range(0,nz):
        m = mat_mult(m, ROT_Z)
    return m

def calculate_rotations():
    rotations = set()
    identity = ((1,0,0),(0,1,0),(0,0,1))

    for nx in range(0,4):
        for ny in range(0,4):
            for nz in range(0,4):
                m = combine_rotations(identity, nx, ny, nz)
                rotations.add(m)

    return rotations

ROTATIONS = calculate_rotations()

def load_scanners(filename):
    file = open(filename)
    try:
        scanners = []
        beacons = []
        id = 0
        for line in file.readlines():
            if "scanner" in line and len(beacons) > 0:
                scanners.append(Scanner(id, beacons))
                beacons = []
                id += 1
            elif "," in line:
                xyz = line.split(",")
                beacons.append((int(xyz[0]), int(xyz[1]), int(xyz[2])))

        scanners.append(Scanner(id, beacons))
        return scanners
    finally:
        file.close()


def fingerprint(beacon1, beacon2):
    d = [
        abs(beacon1[0] - beacon2[0]),
        abs(beacon1[1] - beacon2[1]),
        abs(beacon1[2] - beacon2[2])
    ]
    d.sort()
    return str(d[0]) + "-" + str(d[1]) + "-" + str(d[2])

def calc_fingerprints(beacons):
    fingerprints = {}
    for beacon1 in beacons:
        for beacon2 in beacons:
            if beacon1 != beacon2:
                fingerprints[fingerprint(beacon1, beacon2)] = (beacon1, beacon2)
    return fingerprints

class Scanner:

    def __init__(self, id, beacons):
        self.id = id
        self.beacons = beacons
        self.fingerprints = calc_fingerprints(beacons)
        self.location = (0,0,0)

    def update_beacons(self, rot, d):
        self.beacons = [rotate(b, rot) for b in self.beacons]
        self.beacons = [(b[0]-d[0], b[1]-d[1], b[2]-d[2]) for b in self.beacons]
        self.fingerprints = calc_fingerprints(self.beacons)
        self.location = d

    def identify_beacons(self, fps):
        beacon_counts = {}
        for fp in fps:
            for beacon in self.fingerprints[fp]:
                beacon_counts[beacon] = beacon_counts.get(beacon, 0) + 1

        beacons = set()
        for beacon in beacon_counts:
            if beacon_counts[beacon] > 1:
                beacons.add(beacon)

        return beacons

    def manhatan_dist(self, other):
        l1 = self.location
        l2 = other.location
        return abs(l2[0]-l1[0]) + abs(l2[1]-l1[1]) + abs(l2[2]-l1[2])


def find_best_rotation(target_beacons, scanner_beacons):

    for rot in ROTATIONS:
        rotated_beacons = [rotate(beacon, rot) for beacon in scanner_beacons]
        dists = {}
        for b0 in target_beacons:
            for b1 in rotated_beacons:
                dx = (b1[0] - b0[0], b1[1] - b0[1], b1[2] - b0[2])
                dists[dx] = dists.get(dx, 0) + 1

        for dx in dists:
            if dists[dx] >= 12:
                return (rot, dx)

    return None

class Grid:

    def __init__(self):
        self.beacons = set()
        self.fingerprints = {}
        self.scanners = set()

    def add_scanner(self, scanner):
        self.scanners.add(scanner)
        for beacon in scanner.beacons:
            self.beacons.add(beacon)
        self.fingerprints = calc_fingerprints(self.beacons)

    def overlap(self, other):
        mutual_fps = set(self.fingerprints.keys()).intersection(other.fingerprints.keys())
        beacon_set1 = self.identify_beacons(mutual_fps)
        beacon_set2 = other.identify_beacons(mutual_fps)
        return (beacon_set1, beacon_set2)

    def identify_beacons(self, fps):
        beacon_counts = {}
        for fp in fps:
            for beacon in self.fingerprints[fp]:
                beacon_counts[beacon] = beacon_counts.get(beacon, 0) + 1

        beacons = set()
        for beacon in beacon_counts:
            if beacon_counts[beacon] > 1:
                beacons.add(beacon)

        return beacons

    def best_fit(self, scanners):
        max_overlap = ((), ())
        best_scanner = None
        for scanner in scanners:
            if scanner not in self.scanners:
                overlap = self.overlap(scanner)
                if len(overlap[0]) > len(max_overlap[0]):
                    best_scanner = scanner
                    max_overlap = overlap

        target_beacons, scanner_beacons = max_overlap
        (rot, d) = find_best_rotation(target_beacons, scanner_beacons)

        best_scanner.update_beacons(rot, d)

        return best_scanner

    def beacon_count(self):
        return len(self.beacons)

    def max_scanner_distance(self):
        return max([s0.manhatan_dist(s1) for (s0, s1) in itertools.product(self.scanners, self.scanners)])

def resolve_grid(filename):
    scanners = load_scanners(filename)

    grid = Grid()
    grid.add_scanner(scanners[0])

    while len(grid.scanners) < len(scanners):
        best_fit = grid.best_fit(scanners)
        grid.add_scanner(best_fit)

    return grid
