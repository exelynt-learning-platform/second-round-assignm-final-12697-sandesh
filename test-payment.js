const baseUrl = 'http://localhost:9090/api';

async function testPayment() {
    console.log("1. Registering new user test_payment_user...");
    await fetch(`${baseUrl}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: `payment_user_${Date.now()}`, email: `pay_${Date.now()}@example.com`, password: 'password123' })
    });
    
    // Using a consistent username format for login just below or we can extract the username we just created.
    let username = `pay_${Date.now()}`;
    await fetch(`${baseUrl}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username, email: `${username}@example.com`, password: 'password123' })
    });

    console.log("2. Logging in as new user...");
    let loginRes = await fetch(`${baseUrl}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username, password: 'password123' })
    });
    
    if (!loginRes.ok) {
        console.error("Login failed", await loginRes.text());
        return;
    }
    const { token } = await loginRes.json();
    console.log("-> JWT Token retrieved.");

    console.log("\n2. Adding iPhone (Product 1) to Cart...");
    let addCartRes = await fetch(`${baseUrl}/cart/items`, {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ productId: 1, quantity: 1 })
    });
    if (!addCartRes.ok) {
        console.error("Add cart failed", await addCartRes.text());
        return;
    }
    console.log("-> Cart updated.");

    console.log("\n3. Creating Order from Cart...");
    let createOrderRes = await fetch(`${baseUrl}/orders`, {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ shippingAddress: '123 Test Street, New York, NY' })
    });
    if (!createOrderRes.ok) {
        console.error("Create order failed", await createOrderRes.text());
        return;
    }
    const order = await createOrderRes.json();
    console.log(`-> Order created with ID: ${order.id}, Total Amount: $${order.totalAmount}`);

    console.log("\n4. Requesting Stripe Payment Intent for Order...");
    let paymentIntentRes = await fetch(`${baseUrl}/payments/create-intent/${order.id}`, {
        method: 'POST',
        headers: { 
            'Authorization': `Bearer ${token}`
        }
    });
    
    if (!paymentIntentRes.ok) {
        console.error("Payment Intent failed", await paymentIntentRes.text());
        return;
    }
    const paymentData = await paymentIntentRes.json();
    console.log(`-> SUCCESS! Stripe Client Secret Generated: ${paymentData.clientSecret}`);
    console.log("-> You can use this client secret in your frontend Stripe Elements!");
}

testPayment().catch(console.error);
