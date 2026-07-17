import { Navigate } from "react-router-dom";
import { getRole, getToken } from "../services/auth";

function AdminRoute({ children }) {

    const token = getToken();
    const role = getRole();

    // Not logged in at all -> send to login
    if (!token) {
        return <Navigate to="/login" replace />;
    }

    // Logged in but not an admin -> send to home
    if (role !== "ADMIN") {
        return <Navigate to="/" replace />;
    }

    return children;
}

export default AdminRoute;