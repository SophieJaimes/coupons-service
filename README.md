**Para el Desafío Nivel 1:**

Se generó un API POST que recibe como parámetros una lista de item_ids con sus respectivos precios y el máximo total del cupón.

**Host:** http://34.69.205.108:80/challengeLevel1/

**Request body ejemplo:**

{"items":{"MLA1":100,"MLA2":210,"MLA3":260,"MLA4":80,"MLA5":90},"amount":500}

**Response:**
{"message": "[MLA1, MLA2, MLA4, MLA5]"}

**Nota:** En caso de que llegue más de un ITEM_ID igual, se eliminan los duplicados y solo se tendrá en cuenta una vez cada item.



**Para el Desafío Nivel 2:**

Se generó un API POST que recibe como parámetros una lista de item_ids y el máximo total del cupón

**Host:** http://34.69.205.108:80/coupon/

**Body ejemplo:**

{"items": ["MCO619301169", "MCO619301163", "MCO619301164"],"amount": 200000}

**Nota:** En caso de que el monto total no sea suficiente para comprar al menos un producto, el POST responderá 404-NOT_FOUND




**Para el desafío Nivel 3:**

Se implementó un deployment de kubernetes con un auto-scaling de máximo 10 replicas para asegurar el alto trafico del componente
Se hosteo la aplicación en un cluster de Kubernetes a través de un service tipo Loadbalancer con ip publica: 34.69.205.108

Se implementó la funcionalidad de caché en el request GET https://api.mercadolibre.com/items/
Lo anterior con el fin de que no se consulte dicha información muchas veces seguidas teniendo en cuenta que los usuarios suelen marcar como favoritos los mismos items.


