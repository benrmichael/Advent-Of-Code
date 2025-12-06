import os
import sys

with open(os.path.join(os.getcwd(), 'resources', 'day17.txt')) as file:
    lines = file.readlines()

# 3 Bit Computer
class Computer:
    def __init__(self, A, B, C, program):
        self.A = A
        self.B = B
        self.C = C
        self.program = program
        self.ip = 0
        self.output = []
        self.operations = {0: self.adv, 1: self.bxl, 2: self.bst, 3: self.jnz, 4: self.bxc, 5: self.out, 6: self.bdv, 7: self.cdv}

    def combo_op(self, combo_op):
        if 0 <= combo_op <= 3:
            return combo_op
        if combo_op == 4:
            return self.A
        if combo_op == 5:
            return self.B
        if combo_op == 6:
            return self.C
        raise Exception("Invalid combo operand of 7 received")

    def adv(self, op):
        self.A =  int(self.A / pow(2, self.combo_op(op)))

    def bxl(self, op):
        self.B ^= op

    def bst(self, op):
        self.B = self.combo_op(op) % 8

    def jnz(self, op):
        if self.A != 0:
            self.ip = op

    def bxc(self, op):
        self.B ^= self.C

    def out(self, op):
        self.output.append(self.combo_op(op) % 8)

    def bdv(self, op):
        self.B = int(self.A / pow(2, self.combo_op(op)))

    def cdv(self, op):
         self.C = int(self.A / pow(2, self.combo_op(op)))

    def run(self):
        while 0 <= self.ip < len(self.program):
            opcode = self.program[self.ip]
            operand = self.program[self.ip+1]
            self.ip += 2
            self.operations[opcode](operand)
        return self.output

splitter = ": "
A = int(lines[0].split(splitter)[1])
B = int(lines[1].split(splitter)[1])
C = int(lines[2].split(splitter)[1])
program = list(map(int, lines[4].split(splitter)[1].split(',')))
computer = Computer(A, B, C, program)
output = computer.run()

print(f"puzzle one solution={''.join([str(n) for n in output])}")

# Definitely has something to do with the way the output is calculated by modding by 8...
