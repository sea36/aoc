
class TargetArea:

    def __init__(self, target_x, target_y):
        self.target_x = target_x
        self.target_y = target_y

    def simulate(self, x0, y0):
        x = 0
        y = 0

        dx = x0
        dy = y0

        ymax = 0

        while x < max(self.target_x) and y > min(self.target_y):
            x += dx
            y += dy
            ymax = max(y, ymax)

            if x >= min(self.target_x) and x <= max(self.target_x) and y >= min(self.target_y) and y <= max(self.target_y):
                return ymax

            dx = 0 if dx == 0 else dx - 1
            dy -= 1

        return -1


def run_simulation(target_x, target_y):

    target = TargetArea(target_x, target_y)

    result_count = 0

    ymax = 0
    for x0 in range(1, max(target_x) + 1):

        xmax = ((x0*x0)+x0)/2
        if xmax < min(target_x):
            continue

        for y0 in range(min(target_y), -min(target_y)+1):
            result = target.simulate(x0, y0)
            if result != -1:
                ymax = max(ymax, result)
                result_count += 1

    return {"ymax": ymax, "result_count": result_count}
