# Welcome to inprice parser project

**parser project** is the heart of inprice system and responsible to scrape product info from different well-known e-commerce websites around the world.
It works in multi-threaded way to concurrently parse websites. To be parsed website is sent over RabbitMQ channel.

### parser uses the following tools

| Tools             | Versions | Purpose                                        |
|-------------------|:--------:|------------------------------------------------|
| Java              |   1.11   | Backend development platform                   |
| Selenium          |  4.0.0   | Automated test suit for scraping raw html      |
| HtmlUnit          |  2.52.0  | Headless web scraping library                  |
| JSoup             |  1.14.2  | Simple web scraping library                    |


**Please note that** this project has many common point with **api project**. If you want to see more, please refer to [api project readme](https://github.com/inprice/api/blob/master/README.md)
