
Config:
- there are 3 predefined users:
        user1/pass: user1
        user2/pass: user2
        user3/pass: user3
- application starts on port 8081
- pom file contains all needed dependencies
- java 8

How to run:
- start application
- using POSTMAN or similar make requests to http://localhost:8081/api/product. Use one of the users to authorize (basic authorization)

GET: possible params: name and/or id. Id has priority. If no parameters provided will return whole list of products
example: http://localhost:8081/api/product?name=product1

POST: http://localhost:8081/api/product
body:
{
    "id" : "id1",
    "name" : "product1",
    "price" : 201.65,
    "category": "category1"
}

PUT: http://localhost:8081/api/product
partial body:
{
    "id" : "id1",
    "price" : 301.65,
}
id is mandatory

DELETE: id mandatory parameter
http://localhost:8081/api/product?id=id1


------------------------------------------------------

Application creates HttpServer on port 8081 with a backlog of 50 requests (to limit requests). It has basic authorization: user/pass.
RequestHandler class handles the 4 operations: post (create), get (read), put (update), delete (delete)
ProductRepo class - is a singleton acting as the repository for products.
Product class - object to hold a product
