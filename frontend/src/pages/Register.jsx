import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from 'react-toastify';

function Register() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
        role: "BUYER"
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

            toast.success(response.data);

            navigate("/login");

        } catch (err) {
            toast.error(err.response?.data?.message || "Registration Failed");

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

            <select
                className="form-control mt-3"
                name="role"
                value={form.role}
                onChange={handleChange}
            >
                <option value="BUYER">Buyer</option>
                <option value="SELLER">Seller</option>
            </select>

            <button
                className="btn btn-warning mt-4"
                onClick={register}>

                Register

            </button>

        </div>

    );

}

export default Register;