import React, { useState } from "react";
import "react-toastify/dist/ReactToastify.css";
import { toast } from "react-toastify";

const Login = (props) => {
  const [user, setUser] = useState({ username: "", password: "" });
  const [data, setData] = useState([]);
  const [userID, setUserID] = useState(null);
  const [sessionID, setSessionID] = useState(null);
  const [newUser, setNewUser] = useState(true);
  const [isLogged, setIsLogged] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: user.username,
        password: user.password,
      }),
    };

    e.preventDefault();
    if (newUser) {
      fetch("http://localhost:8080/user/create", requestOptions)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          toast.success("User registered Successfully!!");
          return response.json();
        })
        .then((data) => {
          console.log(data);
          setUser({ username: "", password: "" });
          setIsLogged(true);
        })
        .catch((error) => {
          console.error("There was a problem with the fetch operation:", error);
        });
    } else {
      fetch("http://localhost:8080/user/login", requestOptions)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.json();
        })
        .then((data) => {
          setData(data);
          setUserID(data.userID);
          setSessionID(data.sessionID);
          localStorage.setItem(data.user.userId, data.sessionId);
          const userArray = [data.user.userId, data.user.username];
          localStorage.setItem("currentUser", JSON.stringify(userArray));
          window.location.href = "/user-profile";
        })
        .catch((error) => {
          toast.error("Wrong Credentials");
        });
    }
  };

  return (
    <>
      <div className="app-name">CineMatch</div>
      <div className="auth-wrapper">
        <div className="auth-inner">
          <form onSubmit={handleSubmit}>
            <h3>{newUser ? "Sign Up" : "Sign In"}</h3>
            <div className="mb-3">
              <label htmlFor="username">User Name</label>
              <input
                type="text"
                className="form-control"
                placeholder="User Name"
                name="username"
                id="username"
                value={user.username}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                className="form-control"
                placeholder="Enter password"
                name="password"
                id="password"
                value={user.password}
                onChange={handleChange}
                required
              />
            </div>
            {newUser ? (
              <p
                onClick={() => setNewUser(false)}
                style={{ cursor: "pointer", color: "blue" }}
              >
                Already Registered? Sign In
              </p>
            ) : (
              <p
                onClick={() => setNewUser(true)}
                style={{ cursor: "pointer", color: "blue" }}
              >
                New User? Sign Up
              </p>
            )}
            <div className="d-grid">
              <button type="submit" className="btn btn-primary">
                {newUser ? "Sign Up" : "Login"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default Login;
