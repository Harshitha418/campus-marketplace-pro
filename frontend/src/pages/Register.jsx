import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Register() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
        role: "USER"
    });

    const handleChange = (e) => {

        setForm({
            ...form,
            [e.target.name]: e.target.value
        });

    };

    const register = async () => {

        try {

            const response = await axios.post(
                "http://localhost:8080/api/auth/register",
                form
            );

            alert(response.data);

            navigate("/login");

        } catch (err) {

            alert("Registration Failed");

        }

    };

    return (

        <div className="container mt-5">

            <h2>Register</h2>

            <input
                className="form-control mt-3"
                placeholder="Name"
                name="name"
                onChange={handleChange}
            />

            <input
                className="form-control mt-3"
                placeholder="Email"
                name="email"
                onChange={handleChange}
            />

            <input
                className="form-control mt-3"
                placeholder="Password"
                type="password"
                name="password"
                onChange={handleChange}
            />

            <button
                className="btn btn-warning mt-4"
                onClick={register}>

                Register

            </button>

        </div>

    );

}

export default Register;