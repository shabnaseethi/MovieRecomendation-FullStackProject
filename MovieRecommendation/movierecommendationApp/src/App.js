
import "./App.css";
import Login from "./components/Login/Login";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import UserProfile from "./components/UserProfile/UserProfile";
import { useState } from "react";
import { ToastContainer } from "react-toastify";
import { Navigate } from "react-router-dom";

function App() {
  const [user, setUser] = useState({ userid: "", username: "" });
  const [movie, setMovie] = useState([]);

  const removeAfterTime = 20 * 60 * 1000; 
setTimeout(() => {
  localStorage.removeItem("currentUser");
}, removeAfterTime);

  return (
    <>
      <Router>
        <div className="App">
          <Routes>
            <Route
              exact
              path="/"
              element={<Login user={user} setUser={setUser} />}
            />
            <Route
              path="/user-profile"
              element={
                localStorage.getItem("currentUser") ? (
                  <UserProfile user={user} movie={movie}/>
                ) : (
                  <Navigate to="/" />
                )
              }
            />
          </Routes>
        </div>
      </Router>
      <ToastContainer
        position="bottom-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />
    </>
  );
}

export default App;
