import { Navigate } from "react-router-dom";
import { getRole, getToken } from "../services/auth";

function SellerRoute({ children }) {

    const token = getToken();
    const role = getRole();

    // Not logged in at all -> send to login
    if (!token) {
        return <Navigate to="/login" replace />;
    }

    // Logged in but not a seller -> send to home
    if (role !== "SELLER") {
        return <Navigate to="/" replace />;
    }

    return children;
}

export default SellerRoute;