import pymysql


class Data(object):
    def __init__(self, host='astronaut.snu.ac.kr', port=3306, student_id='20DB_2015_11923'):
        self.db = pymysql.connect(
            host=host, port=3306,
            user=student_id,
            passwd=student_id,
            db=student_id,
            charset='utf8'
        )
        self.cursor = self.db.cursor()

    def __del__(self):
        self.db.close()
        self.cursor.close()

    def check_exist(self, record_type, record_id):
        sql1 = f"""
        SELECT ID FROM
        {record_type}
        WHERE ID={record_id};
        """
        self.cursor.execute(sql1)
        result = self.cursor.fetchone()
        return result is not None

    def check_assigned(self, performance_id):
        sql = f"""
        SELECT count(seat_number)
        FROM seat
        WHERE performance_id = {performance_id};
        """
        self.cursor.execute(sql)
        seat_size = self.cursor.fetchone()[0]
        return seat_size != 0

    # command 1
    def print_all_buildings(self):
        sql = """
        SELECT b.ID,b.name,b.location,b.capacity,count(p.ID) AS assigned
        FROM building as b
        LEFT OUTER JOIN performance as p
        ON p.building_id = b.ID
        GROUP BY b.ID,b.name,b.location,b.capacity;
        """
        self.cursor.execute(sql)
        result = self.cursor.fetchall()
        print_format((('ID', 'name', 'location', 'capacity', 'assigned'),), result)

    # command 2
    def print_all_performances(self):
        sql = """
        SELECT p.ID,p.name,p.type,p.price,count(s.seat_number)
        FROM performance AS p
        LEFT OUTER JOIN seat AS s
        ON s.performance_id = p.ID
        AND s.audience_id IS NOT NULL
        GROUP BY p.ID,p.name,p.type,p.price;
        """
        self.cursor.execute(sql)
        result = self.cursor.fetchall()
        print_format((('ID', 'name', 'type', 'price', 'booked'),), result)

    # command 3
    def print_all_audiences(self):
        sql = """
        SELECT * FROM audience;
        """
        self.cursor.execute(sql)
        result = self.cursor.fetchall()
        print_format((('ID', 'name', 'gender', 'age'),), result)

    # command 4
    def add_building(self, name, location, capacity):
        sql = f"""
        INSERT INTO
        building (name,location,capacity)
        VALUES('{name}','{location}',{capacity});
        """
        self.cursor.execute(sql)
        self.db.commit()
        print('A building is successfully inserted')
        print()

    # command 6
    def add_performance(self, name, type, price):
        sql = f"""
        INSERT INTO
        performance (name,type,price)
        VALUES('{name}','{type}',{price});
        """
        self.cursor.execute(sql)
        self.db.commit()
        print('A performance is successfully inserted')
        print()

    # command 8
    def add_audience(self, name, gender, age):
        sql = f"""
        INSERT INTO
        audience (name,gender,age)
        VALUES('{name}','{gender}',{age});
        """
        self.cursor.execute(sql)
        self.db.commit()
        print('An audience is successfully inserted')
        print()

    # command 5,7,9
    def delete_record(self, record_type, record_id):
        sql = f"""
        DELETE FROM
        {record_type}
        WHERE ID={record_id};
        """
        self.cursor.execute(sql)
        self.db.commit()
        a = 'An' if record_type == 'audience' else 'A'
        print(f'{a} {record_type} is successfully removed')
        print()

    # command 10
    def assign_performance_to_building(self, performance_id, building_id):
        assign_sql = f"""
        UPDATE performance
        SET building_id = {building_id}
        WHERE ID={performance_id}
        AND building_id IS NULL;
        """
        get_capacity_sql = f"""
        SELECT capacity
        FROM building
        WHERE ID={building_id};
        """
        self.cursor.execute(assign_sql)

        # get capacity
        self.cursor.execute(get_capacity_sql)
        building_capacity = self.cursor.fetchone()[0]
        # make seats as much as capacity
        seat = f'({performance_id})'
        seats = ','.join([f'({i},{seat})' for i in range(1, building_capacity + 1)])
        make_seat_sql = f"""
        INSERT INTO seat(seat_number,performance_id)
        VALUES {seats}
        """
        self.cursor.execute(make_seat_sql)
        self.db.commit()
        print('Successfully assign a performance')
        print()

    # command 11
    def book_a_performance(self, performance_id, audience_id, seats: list):
        seat_numbers_condition = ' OR '.join([f'seat_number={seat_num}' for seat_num in seats])
        get_number_of_empty_seats_sql = f"""
        SELECT count(seat_number)
        FROM seat
        WHERE performance_id = {performance_id}
        AND audience_id IS NOT NULL
        AND ({seat_numbers_condition})
        """
        # 입력된 좌석번호 중 실제로 있는 좌석의 수 반환:
        get_number_of_valid_seat_num_sql = f"""
        SELECT count(seat_number)
        FROM seat
        WHERE performance_id = {performance_id}
        AND ({seat_numbers_condition});
        """
        book_a_seat_sql = f"""
        UPDATE seat
        SET audience_id = {audience_id}
        WHERE performance_id = {performance_id} AND ({seat_numbers_condition});
        """
        get_price_sql = f"""
        SELECT price
        FROM performance
        WHERE ID = {performance_id}
        """
        get_age_sql = f"""
        SELECT age
        FROM audience
        WHERE ID = {audience_id}
        """
        self.cursor.execute(get_number_of_valid_seat_num_sql)
        number_of_valid_number = self.cursor.fetchone()[0]
        if number_of_valid_number < len(seats):
            print('Seat number out of range')
            print()
            return
        self.cursor.execute(get_price_sql)
        price = self.cursor.fetchone()[0]
        self.cursor.execute(get_age_sql)
        age = self.cursor.fetchone()[0]
        self.cursor.execute(get_number_of_empty_seats_sql)
        seats_taken = self.cursor.fetchone()[0]
        if seats_taken > 0:
            print('The seat is already taken')
            print()
            return
        self.cursor.execute(book_a_seat_sql)
        if age < 8:
            price = 0
        elif age < 13:
            price = price * 0.5
        elif age < 19:
            price = price * 0.8
        total_price = round(price * len(seats))
        self.db.commit()
        print('Successfully book a performance')
        print(f'Total ticket price is {total_price}')
        print()

    # command 12
    def print_performances_of_building(self, building_id):
        sql = f"""
        SELECT p.ID,p.name,p.type,p.price,count(s.seat_number)
        FROM performance AS p
        LEFT OUTER JOIN seat AS s
        ON s.performance_id = p.ID
        AND s.audience_id IS NOT NULL
        WHERE building_id = {building_id}
        GROUP BY p.ID,p.name,p.type,p.price;
        """
        self.cursor.execute(sql)
        result = self.cursor.fetchall()
        print_format((('ID', 'name', 'type', 'price', 'booked'),), result)

    # command 13
    def print_audiences_of_performance(self, performance_id):
        sql = f"""
        SELECT a.ID,a.name,a.gender,a.age
        FROM (
            SELECT DISTINCT audience_id
            FROM seat
            WHERE performance_id={performance_id}
        ) AS l
        JOIN audience AS a
        ON l.audience_id = a.ID;
        """
        self.cursor.execute(sql)
        result = self.cursor.fetchall()
        print_format((('ID', 'name', 'gender', 'age'),), result)

    # command 14
    def print_booking_status_of_performance(self, performance_id):
        sql = f"""
        SELECT seat_number,audience_id FROM seat
        WHERE performance_id={performance_id};
        """
        self.cursor.execute(sql)
        result = self.cursor.fetchall()
        print_format((('seat_number', 'audience_id'),), result)

    # command 16
    def reset_database(self):
        while True:
            check = input('Are you sure you want to reset database? [y/n]')
            if check == 'n':
                return
            elif check == 'y':
                sql_array = [
                    'DROP TABLE IF EXISTS `seat`;',
                    'DROP TABLE IF EXISTS `performance` ;',
                    'DROP TABLE IF EXISTS `building` ;',
                    'DROP TABLE IF EXISTS `audience` ;',
                    'CREATE TABLE `audience` ( '
                    '`ID` INT(11) NOT NULL AUTO_INCREMENT, '
                    '`name` VARCHAR(200) NULL DEFAULT NULL, '
                    '`gender` VARCHAR(45) NULL DEFAULT NULL, '
                    '`age` INT(11) UNSIGNED NULL DEFAULT NULL, '
                    'PRIMARY KEY (`ID`));',
                    'CREATE TABLE `building` ( '
                    '`ID` INT(11) NOT NULL AUTO_INCREMENT, '
                    '`name` VARCHAR(200) NULL DEFAULT NULL,'
                    '`location` VARCHAR(200) NULL DEFAULT NULL,'
                    '`capacity` INT(10) UNSIGNED NULL DEFAULT NULL,'
                    'PRIMARY KEY (`ID`));',
                    'CREATE TABLE `performance` ('
                    '`ID` INT(11) NOT NULL AUTO_INCREMENT,'
                    '`name` VARCHAR(200) NULL DEFAULT NULL,'
                    '`type` VARCHAR(200) NULL DEFAULT NULL,'
                    '`price` INT(10) UNSIGNED NULL DEFAULT NULL,'
                    '`building_id` INT(11) NULL DEFAULT NULL,'
                    'PRIMARY KEY (`ID`));',
                    'CREATE TABLE `seat` ('
                    '`seat_number` INT(11) NOT NULL,'
                    '`performance_id` INT(11) NOT NULL,'
                    '`audience_id` INT(11) NULL DEFAULT NULL,'
                    'PRIMARY KEY (`seat_number`, `performance_id`));',
                    'ALTER TABLE `performance`'
                    'ADD CONSTRAINT `performances_to_building`'
                    'FOREIGN KEY (`building_id`) '
                    'REFERENCES `20DB_2015_11923`.`building`(`ID`) '
                    'ON DELETE CASCADE '
                    'ON UPDATE NO ACTION;',
                    'ALTER TABLE `seat`'
                    'ADD CONSTRAINT `seats_to_performance`'
                    'FOREIGN KEY (`performance_id`)'
                    'REFERENCES `20DB_2015_11923`.`performance`(`ID`)'
                    'ON DELETE CASCADE '
                    'ON UPDATE NO ACTION;',
                    'ALTER TABLE `seat`'
                    'ADD CONSTRAINT `seats_to_audience`'
                    'FOREIGN KEY (`audience_id`)'
                    'REFERENCES `20DB_2015_11923`.`audience`(`ID`)'
                    'ON DELETE SET NULL '
                    'ON UPDATE NO ACTION;'
                ]
                for sql in sql_array:
                    self.cursor.execute(sql)
                print('done!')
                print()
                return
            else:
                print('Invalid input')


def show_actions():
    print('============================================================')
    print('1. print all buildings')
    print('2. print all performances')
    print('3. print all audiences')
    print('4. insert a new building')
    print('5. remove a building')
    print('6. insert a new performance')
    print('7. remove a performance')
    print('8. insert a new audience')
    print('9. remove an audience')
    print('10. assign a performance to a building')
    print('11. book a performance')
    print('12. print all performances which assigned at a building ')
    print('13. print all audiences who booked for a performance')
    print('14. print ticket booking status of a performance')
    print('15. exit')
    print('16. reset database')
    print('============================================================')


def print_format(attributes, records):
    col_width = [len(attribute) for attribute in attributes[0]]
    for record in records + attributes:
        for idx, att in enumerate(record):
            if len(str(att)) > col_width[idx]:
                col_width[idx] = len(str(att))
    col_width = [width + 2 for width in col_width]
    print('--------------------------------------------------------------------------------')
    for column in attributes:
        print(' '.join([f'{att: <{width}}' if att is not None else '' for width, att in zip(col_width, column)]))
    print('--------------------------------------------------------------------------------')
    for record in records:
        print(' '.join([f'{att: <{width}}' if att is not None else '' for width, att in zip(col_width, record)]))
    print('--------------------------------------------------------------------------------')
    print()

if __name__ == '__main__':
    data: Data = Data()
    # data._prepare_schema()
    show_actions()
    while True:
        command = int(input('Select your action: '))
        if command == 1:
            data.print_all_buildings()

        elif command == 2:
            data.print_all_performances()

        elif command == 3:
            data.print_all_audiences()

        elif command == 4:
            name = input('Building name: ')[:200]
            location = input('Building location: ')[:200]
            capacity = int(input('Building capacity: '))
            if capacity < 1:
                print('Capacity should be more than 0')
                print()
                continue
            data.add_building(name, location, capacity)

        elif command == 5:
            building_id = int(input('Building ID: '))
            if not data.check_exist('building', building_id):
                print(f'Building {building_id} doesn’t exist')
                print()
                continue
            data.delete_record('building', building_id)

        elif command == 6:
            name = input('Performance name: ')[:200]
            performance_type = input('Performance type: ')[:200]
            price = int(input('Performance price: '))
            if price < 0:
                print('Price should be 0 or more')
                print()
                continue
            data.add_performance(name, performance_type, price)

        elif command == 7:
            performance_id = int(input('Performance ID: '))
            if not data.check_exist('performance', performance_id):
                print(f'Performance {performance_id} doesn’t exist')
                print()
                continue
            data.delete_record('performance', performance_id)

        elif command == 8:
            name = input('Audience name: ')[:200]
            gender = input('Audience gender: ')
            if gender != 'M' and gender != 'F':
                print('Gender should be \'M\' or \'F\'')
                print()
                continue
            age = int(input('Audience age: '))
            if age < 1:
                print('Age should be more than 0')
                print()
                continue
            data.add_audience(name, gender, age)

        elif command == 9:
            audience_id = int(input('Audience ID: '))
            if not data.check_exist('audience', audience_id):
                print(f'Audience {audience_id} doesn’t exist')
                print()
                continue
            data.delete_record('audience', audience_id)

        elif command == 10:
            performance_id = int(input('Performance ID: '))
            if not data.check_exist('performance', performance_id):
                print(f'Performance {performance_id} doesn’t exist')
                print()
                continue
            if data.check_assigned(performance_id):
                print(f'Performance {performance_id} is already assigned')
                print()
                continue
            building_id = int(input('Building ID: '))
            if not data.check_exist('building', building_id):
                print(f'Building {building_id} doesn’t exist')
                print()
                continue
            data.assign_performance_to_building(performance_id, building_id)

        elif command == 11:
            performance_id = int(input('Performance ID: '))
            if not data.check_exist('performance', performance_id):
                print(f'Performance {performance_id} doesn’t exist')
                print()
                continue
            if not data.check_assigned(performance_id):
                print(f'Performance {performance_id} isn\'t assigned')
                print()
                continue
            audience_id = int(input('Audience ID: '))
            if not data.check_exist('audience', audience_id):
                print(f'Audience {audience_id} doesn’t exist')
                print()
                continue
            seat_numbers_string = input('Seat number: ')
            seat_numbers = [int(seat_number) for seat_number in seat_numbers_string.split(',')]
            data.book_a_performance(performance_id, audience_id, seat_numbers)

        elif command == 12:
            building_id = int(input('Building ID: '))
            if not data.check_exist('building', building_id):
                print(f'Building {building_id} doesn’t exist')
                print()
                continue
            data.print_performances_of_building(building_id)

        elif command == 13:
            performance_id = int(input('Performance ID: '))
            if not data.check_exist('performance', performance_id):
                print(f'Performance {performance_id} doesn’t exist')
                print()
                continue
            data.print_audiences_of_performance(performance_id)

        elif command == 14:
            performance_id = int(input('Performance ID: '))
            if not data.check_exist('performance', performance_id):
                print(f'Performance {performance_id} doesn’t exist')
                print()
                continue
            data.print_booking_status_of_performance(performance_id)

        elif command == 15:
            print('Bye!')
            break

        elif command == 16:
            data.reset_database()

        else:
            print('Invalid action')
