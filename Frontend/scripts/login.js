// var loggedInUser = JSON.parse(localStorage.getItem("login-user")) || null
// function onPageLoadFun(){

//   if(loggedInUser){
//      window.location.href = "index.html"
//   }

// }

document.querySelector("form").addEventListener("submit", function () {
  event.preventDefault();

  var username = document.getElementById("username");
  var userNameError = document.getElementById("usernameError");

  if (username.value.trim() == "") {
    userNameError.style.display = "block";
  } else {
    userNameError.style.display = "none";
  }

  var password = document.getElementById("password");
  var passwordError = document.getElementById("passwordError");

  if (password.value.trim() == "") {
    passwordError.style.display = "block";
  } else {
    passwordError.style.display = "none";
  }

  if (username.value.trim() == "" || password.value.trim() == "") {
    alert("Please fill all fielsd");
    return;
  }

  let loginUser = {
    email: username.value,
    password: password.value,
  };

  loginCustomer(loginUser);
});

let loginCustomer = async (loginUser) => {
  // Encode email and password in Base64
  //let credentials = btoa(`${loginUser.email}:${loginUser.password}`);

  try {
    let res = await fetch(`http://localhost:8080/customers/signin`, {
      method: "GET",
      Authorization: {
        email: loginUser.email,
        password: loginUser.password,
      },
    });

    let data = await res.json();
    console.log(data);

    if (res.ok) {
      let data = await res.text(); // Assuming the response is a plain text message
      console.log("Login successful:", data);
      // Handle success (e.g., store token, redirect, etc.)
    } else {
      console.error("Login failed:", res.statusText);
      // Handle error (e.g., display error message)
    }
  } catch (error) {
    console.error("Error occurred:", error);
    // Handle network or other errors
  }
};
