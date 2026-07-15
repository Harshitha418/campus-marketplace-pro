import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import { saveLogin } from "../services/auth";

function Login() {

    const navigate = useNavigate();

    const [email, setEmail] = useState("");

    const [password, setPassword] = useState("");

    const [message, setMessage] = useState("");

    const handleLogin = async (e) => {

        e.preventDefault();

        try {

            const response = await api.post("/auth/login", {

                email,
                password

            });

            saveLogin(response.data.token, email, response.data.role);

            setMessage("Login Successful!");

            if (response.data.role === "SELLER") {
                navigate("/seller");
            } else {
                navigate("/");
            }

        }

        catch (error) {

            if (error.response) {

                setMessage(error.response.data);

            }

            else {

                setMessage("Login Failed");

            }

        }

    };

    return (

        <div className="container mt-5">

            <div className="row justify-content-center">

                <div className="col-md-5">

                    <div className="card shadow">

                        <div className="card-body">

                            <h2 className="text-center mb-4">

                                Login

                            </h2>

                            <form onSubmit={handleLogin}>

                                <div className="mb-3">

                                    <label>Email</label>

                                    <input
                                        type="email"
                                        className="form-control"
                                        value={email}
                                        onChange={(e) =>
                                            setEmail(e.target.value)}
                                        required
                                    />

                                </div>

                                <div className="mb-3">

                                    <label>Password</label>

                                    <input
                                        type="password"
                                        className="form-control"
                                        value={password}
                                        onChange={(e) =>
                                            setPassword(e.target.value)}
                                        required
                                    />

                                </div>

                                <button
                                    className="btn btn-dark w-100">

                                    Login

                                </button>

                            </form>

                            <p
                                className="text-center text-danger mt-3">

                                {message}

                            </p>

                            <p className="text-center">

                                Don't have an account?

                                <Link to="/register">

                                    Register

                                </Link>

                            </p>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    );

}

export default Login;