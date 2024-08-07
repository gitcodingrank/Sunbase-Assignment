let form = document.querySelector("form");
let formToggle = false;


let addCustomers = async (customer) => {
  const response = await fetch("http://localhost:8080/customers/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(customer),
  });
  if(response.status=="201"){
    alert("Customer Registered Successfully.")
    form.style.display = "none"

  }
};

document.getElementById("addCustomer").addEventListener("click", function () {
  formToggle = !formToggle;
  formToggle ? (form.style.display = "block") : (form.style.display = "none");
});

document.querySelector("form").addEventListener("submit", function () {
  event.preventDefault();
  let customer = {
    firstName: form.first_name.value,
    lastName: form.last_name.value,
    password: form.password.value,
    street: form.street.value,
    address: form.address.value,
    city: form.city.value,
    state: form.state.value,
    email: form.email.value,
    phone: form.phone.value,
    role: "user",
  };

//   console.log(customer);
  addCustomers(customer);
});
