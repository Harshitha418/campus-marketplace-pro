import { Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Account from "./pages/Account";
import ProductDetails from "./pages/ProductDetails";
import Cart from "./pages/Cart";
import Wishlist from "./pages/Wishlist";
import Orders from "./pages/Orders";
import AdminDashboard from "./pages/AdminDashboard";
import SellerDashboard from "./pages/SellerDashboard";
import AddProduct from "./pages/AddProduct";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import SellerRoute from "./components/SellerRoute";
import AdminRoute from "./components/AdminRoute";
import OrderDetails from "./pages/OrderDetails";
import AdminOrderDetail from "./pages/AdminOrderDetail";

function App() {

    return (

        <div
            className="d-flex flex-column"
            style={{ minHeight: "100vh" }}
        >

            <Navbar />

            <main className="flex-grow-1" style={{ paddingTop: "70px" }}>

                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/product/:id" element={<ProductDetails />} />
                    <Route path="/cart" element={<Cart />} />
                    <Route path="/account" element={<Account />} />
                    <Route path="/wishlist" element={<Wishlist />} />
                    <Route path="/orders" element={<Orders />} />
                    <Route path="/admin" element={
                        <AdminRoute><AdminDashboard /></AdminRoute>
                    } />
                    <Route path="/seller" element={
                        <SellerRoute><SellerDashboard /></SellerRoute>
                    } />
                    <Route path="/add-product" element={
                        <SellerRoute><AddProduct /></SellerRoute>
                    } />
                    <Route path="/admin/order/:orderId" element={
                        <AdminRoute><AdminOrderDetail /></AdminRoute>
                    } />
                    <Route path="/orders/:orderId" element={<OrderDetails />} />
                </Routes>

            </main>

            <Footer />

        </div>

    );

}

export default App;