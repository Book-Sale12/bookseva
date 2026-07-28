#!/bin/bash

# Login as Alice
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@college.edu","password":"password123"}' | jq -r '.data.accessToken')

echo "Token: $TOKEN"

echo "Current Cart:"
curl -s -X GET http://localhost:8080/api/v1/cart \
  -H "Authorization: Bearer $TOKEN" | jq

echo "Add Book 12 to Cart:"
curl -s -X POST http://localhost:8080/api/v1/cart/items \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"bookId": 12, "quantity": 1}' | jq

echo "Check Cart Again:"
curl -s -X GET http://localhost:8080/api/v1/cart \
  -H "Authorization: Bearer $TOKEN" | jq

