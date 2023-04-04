# Табличный редактор
Табличный редактор с поддержкой математических формул. Реализован на библиотеке Swing.
Для запуска необходимо выполтить метод main в классе src/main/java/tableeditor/Main.java

### DSL Формул
- Унарные операции: + , -
- Бинарные операции: + , - , * , / , %
- Именованные функции: pow() , sqrt(), abs()
- Скобки (, ) для изменения приоритета операций
- Операндами могут быть целые числа, действительные числа и строковые ссылки на ячейки вида "A1", где A-имя столбца 1-номер строки.
 
### UI
#### Верхняя панель:
- Поле для отображения номера выделенной ячейки
- Кнопка "F(x)" для выбора доступной формулы из списка
- Поле для отображения и редактирования содержимого выделенной ячейки. Всегда отображает текст формулы.

#### Таблица
- По умолчанию содержит 100x100 ячеек
- Если в ячейке текст начинается с символа '=' то он интерпретируется как формула и после окончания редактирования вычисляется значение формулы
- В ячейках отображается вычисленное значение формулы или текст, если он не содержит формулу
- Если произошла ошибка при вычислении формулы, то в ячейке отображается "#ERROR"
- Если в ячейке изменяется значение, то пересчитываются все формулы связанные с ней.

#### Нижняя панель
- Отображает текст ошибки произошедшей при вычислении формулы выделенной ячейки 

## Обрабатываемые типы ошибок при вычислении формул

- 1: Invalid syntax from %d to %d
- 2: Invalid syntax character: '%s' at position %d
- 3: Bracket count not correct
- 4: Function 'foo()' not found
- 5: Invalid cell link name '%s'
- 6: Error. cannot cast to Number value '%s'
- 7: Error. Reflexive link
- 8: Error. Iterative link
- 9: Error. cast to Number value from cell '%s'
- 10: Function %s should have %d input params
- 11: Syntax error token:
- 12: Unknown operation
- 13: Unsupported unary operation %s
- 14: Second operand binary operation '%s' not defined
- 15: Cell link '%s' out of table bound


## Backlog для дальнейшего развития
#### формулы:
- добавить операции сравнения > < >= <= == != 
- добавить логические операции ! && ||
- добавить условные операторы  "условие ? значение1 : значение2", "IF ELSE"
- расширить список именованных функций 
- добавить функции над интервалами ячеек вида "SUM(A1:D10)"
- добавить строгую проверку на тип входящих параметров

#### ui:
- вставлять имя ячейки в текст по клику на ячейку в режиме написания формулы
- создать мастер для написания формул с выбором и описанием всех именованных функций
- сделать выделение ячеек множественным
- добавить возможность выделения интервала ячеек написав его в поле для отображения номера выделенной ячейки 