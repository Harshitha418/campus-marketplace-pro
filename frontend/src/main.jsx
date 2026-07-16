import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "./styles/global.css";
import App from "./App";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from "react-toastify";
import { CartProvider } from "./context/CartContext";

ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <CartProvider>
      <App />
    </CartProvider>
    <ToastContainer
        position="bottom-right"
        autoClose={2500}
    />
  </BrowserRouter>
);