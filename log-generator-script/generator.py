#!/usr/bin/python

# DSBDA. Homework 2.
# 13 Nov 2020
# Syslog messages generator.
# Author: Nikitin Svyatoslav, M19-512

import datetime
import string
import random

levels = ["emerg", "panic", "alert", "crit", "error", "err", "warning", "warn", "notice", "info", "debug"]

# EXAMPLE: Oct 21 08:31:39 moonlight-pc journal[428487]: Theme parsing error: colors.css:71:44: Invalid number for color value
# FORMAT:  datetime, hostname, process[pid]: message
class Message:
    """
        Класс, представляющий сообщение системного лога
    """

    def __init__(self, datetime, hostname, process, pid, message, level):
        """
            Default constructor
        Args:
            datetime (datetime):    Дата и время сообщения
            hostname (string):      Имя хоста
            process (string):       Имя процесса
            pid (integer):          Уникальный идентификатор процесса
            message (string):       Содержимое сообщения
        """
        self.datetime = datetime
        self.hostname = hostname
        self.process = process
        self.pid = pid
        self.message = message
        self.level = level

    def __str__(self):
        """

        Returns:
            string: строковое представление сообщения системного лога
        """
        dt_formatted = self.datetime.strftime("%b %d %H:%M:%S")
        return ("%s %s %s[%s]: %s %s" % (dt_formatted, self.hostname, self.process, self.pid, self.message, self.level))

    def size(self):
        """
            Метод, возвращающий длину сообщения.
        Returns:
            string: Длина сообщения системного лога.
        """
        return len(str(self))


class Syslog:
    """
        Класс, представляющий системный лог
    """

    def __init__(self):
        self.messages = []

    def __str__(self):
        return "\n".join(self.messages)

    def gen_string(self, size):
        """
            Метод, возвращающий случайную строку
        Args:
            size (integer): Требуемая длина строки

        Returns:
            string: Случайная строка длины, переданной в аргументе size
        """
        return ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(size))

    def generate(self, start_dttm, step, limit):
        """
            Метод, заполняющий системный лог сообщениями, начиная от start_dttm с шагом step с.
            Заполнение идёт до тех пор, пока лог не станет размером, указанном в limit (МБ)
            Остальные параметры заполняются случайно
        Args:
            start_dttm (datetime): Дата и время первого сообщения
            step (int): Шаг между сообщениями в секундах
            limit (int): Размер генерируемого массива сообщений в мегабайтах
        """
        create_dttm = start_dttm
        log_size = 0
        while log_size <= (limit * 1024 * 1024):
            hostname = "vm-" + str(random.randint(1000, 9999))
            process = self.gen_string(10)
            pid = random.randint(100000, 999999)
            message = self.gen_string(50)
            level = levels[random.randrange(len(levels))]
            msg = Message(create_dttm, hostname, process, pid, message, level)

            create_dttm += datetime.timedelta(seconds=step)
            self.messages.append(msg)
            log_size += msg.size()

    def write(self, filename):
        """
            Сохранение системного лога в файл
        Args:
            filename (string): имя выходного файла
        """
        f = open(filename, 'w')
        for msg in self.messages:
            f.write(str(msg) + "\n")
        f.close()


if __name__ == "__main__":
    print("Started!")
    # TODO: replace assignments to getopt mechanism
    log_size = 1
    start_dttm = datetime.datetime(year=2020, month=10, day=1, hour=10, minute=0, second=0)
    out_filename = "log"
    step = 37

    log = Syslog()
    log.generate(start_dttm, step, log_size)
    log.write(out_filename)
