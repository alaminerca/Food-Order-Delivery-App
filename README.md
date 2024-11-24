# Food Order & Delivery App

A comprehensive food ordering and delivery management system built with Android (Java). The application serves three types of users: customers who order food, administrators who manage the menu and orders, and delivery agents who handle order deliveries.

## Features

### Customer Features
- Browse menu items
- Add items to cart
- Place and track orders
- View order history
- Real-time delivery status updates

### Admin Features
- Manage menu items (CRUD operations)
- View and manage orders
- Assign delivery agents
- Monitor delivery status
- User management

### Delivery Agent Features
- View assigned orders
- Update delivery status
- Mark orders as delivered
- Manage availability status

## Screenshots

### Authentication
| Login Screen | Registration Screen |
|--------------|-------------------|
| ![Login](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/login.jpg) | ![Register](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/registration.jpg) |

### Customer Views
| Menu Screen | Cart Screen | Order Tracking |
|-------------|-------------|----------------|
| ![Menu](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/customerMenu.jpg) | ![Menu](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/customerMenu.jpg) | ![Cart](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/customerCart.jpg) |

### Admin Dashboard
| Menu Management | Order Management |
|----------------|------------------|
| ![Admin Menu](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/adminMenuManage.jpg) | ![Admin Orders](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/adminOrderManage.jpg) |

### Delivery Agent Interface
| Active Orders | Delivery Status |
|--------------|----------------|
| ![Delivery List](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/activeDeliveries.jpg) | ![Delivery Status](https://github.com/alaminerca/Food-Order-Delivery-App/blob/main/screenshots/deliveryStatus.jpg) |

## Technologies Used
- Android SDK
- Java
- SQLite with Room Persistence Library
- MVVM Architecture
- Material Design Components

## Setup & Installation
1. Clone the repository
```bash
git clone https://github.com/alaminerca/Food-Order-Delivery-App.git
```

2. Open in Android Studio
3. Sync Gradle files
4. Run on emulator or physical device

## Test Accounts ( or you can create)
```
Admin:
- Email: admin@admin.com
- Password: admin123

Customer:
- Email: test@test.com
- Password: test123

Delivery:
- Email: delivery@test.com
- Password: delivery123
```

## Build & Run
- Minimum SDK Version: 24
- Target SDK Version: 34
- Compile SDK Version: 34

To build and run the project:
1. Open in Android Studio
2. Connect Android device or start emulator
3. Click 'Run' (or press Shift + F10)

## Project Structure
```
app/
├── java/
├── screenshots/
│   └── com.example.foodorderapp/
│       ├── activities/
│       ├── adapters/
│       ├── database/
│       ├── fragments/
│       ├── models/
│       └── utils/
└── res/
    ├── layout/
    ├── menu/
    ├── values/
    └── drawable/
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)

## Author
- [Alamine Mouhamad](https://github.com/alaminerca)

## Acknowledgments
- Professor Tanweer Alam, Islamic University of Madinah
- Claude AI Assistant by Anthropic for development assistance