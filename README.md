# Dokumentacja REST API dla mikrousług obsługujących system sklepu.

## Opis

Projekt został stworzony na bazie trzech mikroserwisów: product-service, stock-service oraz order-service.
Serwis produktów zarządza bazą danych produktów dostępnych do zakupu w sklepie. Serwis magazynu zarządza ilością
dostępnych produktów w magazynie. Serwis zamówień komunikuje się z pozostałymi serwisami w celu utworzenia pary 
produkt : ilość, przechowuje produkty i ich ilości w jednym obiekcie zamówienia.

## Testowanie

API najlepiej testować przy użyciu programu POSTMAN. Tak też postąpie w przypadku tej dokumentacji.

## Product - service

Aplikacja uruchamiana jest na porcie 8080.

### GET /api/products

![img.png](images/img.png)

Status: 200 OK

Endpoint wyświetla listę wszystkich produktów dostępnych w sklepie oraz dodatkowe informacje o nich.

### GET /api/products/id/{id}

![img_1.png](images/img_1.png)

Status: 200 OK

Endpoint wyświetla produkt o podanym {id}.

### GET /api/products/name/{name}
{name} w formacie: Iphone%2016

![img_3.png](images/img_3.png)

Status: 200 OK

Endpoint wyświetla produkt o podanym {name}.

### POST /api/products

{ \
    "name": "Iphone SE", \
    "description": "iphone se - silver", \
    "price": 500.00 \
}

![img_2.png](images/img_2.png)

Status: 201 Created

Endpoint dodaje do bazy danych json przekazany w metodzie POST.

### PUT /api/products/{id}

{ \
    "name": "Iphone SE", \
    "description": "iphone se - golden rose", \
    "price": 500.00 \
}

![img_4.png](images/img_4.png)

Endpoint aktualizuje produkt o podanym {id} według schematu w wysłanym jsonie.

Status: 200 OK

### DELETE /api/products/{id}

Endpoint usuwa produkt o podanym {id}.

Status: 200 OK

## Walidacja

### Nieprawidłowy URL

![img_5.png](images/img_5.png)

Status: 404 Not Found

### Próba dodania produktu istniejącego w bazie danych.

![img_7.png](images/img_7.png)

### Próba edycji produktu na taki, który już istnieje.

![img_8.png](images/img_8.png)

### Próba zaktulaizowania produktu o nieistniejącym id.

![img_9.png](images/img_9.png)

### Próba usunięcia produktu o nieistniejącym id.

![img_10.png](images/img_10.png)

## Stock - service

Aplikacja uruchamiana jest na porcie 8081.
Stock - service komunikuje się z produkt - service, aby dowiedzieć się czy wprowadzane produkty w obiektach
typu stock znajdują się w bazie danych.

### GET /api/stocks

![img_11.png](images/img_11.png)

Endpoint zwraca informacje o produktach znajdujących się w bazie danych w połączeniu 
z ich ilością dostępna w bazie danych.

Status: 200 OK.

### Pozostałe endpointy działają dokładnie tak samo, jak w przypadku product - service.

## Walidacja

### Nieprawidłowy URL

![img_12.png](images/img_12.png)

### Próba uwtorzenia obiektu z nieistniejącym w bazie danych produktów produktem.

![img_15.png](images/img_15.png)

### Próba uwtorzenia obiektu z produktem już istniejącym w bazie danych stock.

![img_16.png](images/img_16.png)

### Próba wprowadzenia niepoprawnych danych w metodzie PUT.

![img_17.png](images/img_17.png)

![img_18.png](images/img_18.png)

## Order - service

Aplikacja uruchamiana jest na porcie 8082.
Order - service komunikuje się z product - service, aby sprawdzić, czy produkt dodawany w obiekcie order
znajduje się w bazie danych produktów. Order - service komunikuje się również ze stock - service, aby sprawdzić, czy 
produkty są dostępne.

### GET /api/orders

![img_19.png](images/img_19.png)

### GET /api/orders/{id}

![img_20.png](images/img_20.png)

### POST /api/orders

![img_25.png](images/img_25.png)

### PUT /api/orders/{id}

![img_26.png](images/img_26.png)

### DELETE /api/orders/{id}

Status: 200 OK

## Walidacja

### Próba dodania produktu, którego quantity wynosi 0:

![img_27.png](images/img_27.png)

### Próba zaktualizowania na produkt, którego quantity wynosi 0:

![img_28.png](images/img_28.png)

## Dodatkowe funkcjonalności

Do każdego serwisu dołączony jest MVC Controller umożliwiający edycję i przeglądanie danych w przeglądarce.
Order - service nie posiada możliwości dodawania nowych danych w przeglądarce. Serwisy nie posiadają żadnego sposobu autoryzacji.
Do przechowywania danych zostały użyte 3 instancje bazy H2.



