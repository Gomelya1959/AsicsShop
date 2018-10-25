package org.springmvcshoppingcart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springmvcshoppingcart.dao.*;
import org.springmvcshoppingcart.entity.Account;
import org.springmvcshoppingcart.entity.Product;
import org.springmvcshoppingcart.entity.Reservation;
import org.springmvcshoppingcart.model.*;
import org.springmvcshoppingcart.util.Utils;
import org.springmvcshoppingcart.validator.CustomerInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
// Enable Hibernate Transaction.
@Transactional
// Need to use RedirectAttributes
@EnableWebMvc
public class MainController {

    private OrderInfo orderInfoForDelete = null;

    private String name;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private DiscountDAO discountDAO;

    @Autowired
    private RegularCustomerDiscountDAO regularCustomerDiscountDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CourierDAO courierDAO;

    @Autowired
    private CustomerInfoValidator customerInfoValidator;

    @Autowired
    private ReservationDAO reservationDAO;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        // For Cart Form.
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {

        }
        // For Customer Form.
        // (@ModelAttribute("customerForm") @Validated CustomerInfo
        // customerForm)
        else if (target.getClass() == CustomerInfo.class) {
            dataBinder.setValidator(customerInfoValidator);
        }

    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/registration")
    public String registration() {
        return "registration";
    }

    // Product List page.
    // Danh sách sản phẩm.
    @RequestMapping(value = { "/productList" })
    public String listProductHandler(Model model, HttpServletRequest request,
                                     @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "codeProduct", defaultValue = "") String codeProduct,
                                     @RequestParam(value = "deleteDiscountCode", defaultValue = "") String deleteDiscountCode) {

        reservationDAO.deleteReservation();

        if (request.getParameter("codeProduct") != null && !request.getParameter("codeProduct").equals("")) {
            model.addAttribute("codeProduct", codeProduct);
        }

        if (request.getParameter("deleteDiscountCode") != null && !request.getParameter("deleteDiscountCode").equals("")) {
            Product product = productDAO.findProduct(deleteDiscountCode);
            product.setPrice(product.getPrice()*100/(100 - product.getDiscount()));
            product.setDiscount(0.0);
            productDAO.updateProduct(product);
        }

        final int maxResult = 5;
        final int maxNavigationPage = 10;

        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");

        PaginationResult<ProductInfo> result = productDAO.queryProducts(page, //
                maxResult, maxNavigationPage, likeName);

        List<ProductInfo> productInfoList = new ArrayList<>();

        if (cartInfo != null) {
            for (ProductInfo productInfo : result.getList()) {
                for (CartLineInfo cartLineInfo : cartInfo.getCartLines()) {
                    if (cartLineInfo.getProductInfo().getCode().equals(productInfo.getCode())) {
                        if (productInfo.getSurplus() - cartLineInfo.getQuantity() >= 0) {
                            productInfo.setSurplus(productInfo.getSurplus() - cartLineInfo.getQuantity());
                        }
                        break;
                    }
                }
                productInfoList.add(productInfo);
            }
        }
        else {
            productInfoList = result.getList();
        }
        model.addAttribute("listProducts", productInfoList);
        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @RequestMapping(value = { "/productList" }, method = RequestMethod.POST)
    @Transactional(propagation = Propagation.NEVER)
    public String listProductView(HttpServletRequest request,
                                  @RequestParam(value = "codeProduct", defaultValue = "") String codeProduct) {
        if (request.getParameter("codeProduct") != null && !request.getParameter("codeProduct").equals("")) {
            Product product = productDAO.findProduct(codeProduct);
            System.out.println(request.getParameter("discount"));
            double discount = Double.valueOf(request.getParameter("discount"));
            product.setDiscount(discount);
            product.setPrice(product.getPrice() - product.getPrice()*discount/100);
            productDAO.updateProduct(product);
        }
        return "redirect:/ productList";
    }


    @RequestMapping({ "/courierList" })
    public String listCourierHandler(Model model, //
                                     @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 5;
        final int maxNavigationPage = 10;

        PaginationResult<CourierInfo> result = courierDAO.queryCouriers(page, //
                maxResult, maxNavigationPage, likeName);

        model.addAttribute("paginationCourier", result);
        return "courierList";
    }

    @RequestMapping({ "/buyProduct" })
    public String listProductHandler(HttpServletRequest request, Model model, //
                                     @RequestParam(value = "code", defaultValue = "") String code) {

        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {

            // Cart info stored in Session.
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.addProduct(productInfo, 1);

        }
        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }

    @RequestMapping({ "/shoppingCartRemoveProduct" })
    public String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (product != null) {

            // Cart Info stored in Session.
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.removeProduct(productInfo);

        }
        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }

    // POST: Update quantity of products in cart.
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @ModelAttribute("cartForm") CartInfo cartForm) {
        ReservationInfo reservationInfo = reservationDAO.findReservationInfo(request.getParameter("codeForReservation"));

        if (reservationInfo != null) {
            CartLineInfo cartLineInfo = new CartLineInfo();
            ProductInfo productInfo = productDAO.findProductInfo(reservationInfo.getProductCode());
            if (reservationInfo.getNumbers() <= productInfo.getSurplus()) {
                cartLineInfo.setQuantity(reservationInfo.getNumbers());
                productInfo.setSurplus(productInfo.getSurplus() - reservationInfo.getNumbers());
            }
            else {
                cartLineInfo.setQuantity(productInfo.getSurplus());
                productInfo.setSurplus(0);

            }
            cartLineInfo.setProductInfo(productInfo);
            cartLineInfo.setDiscountInfoList(discountDAO.getListDiscounts(reservationInfo.getProductCode()));
            List<CartLineInfo> cartLineInfoList = new ArrayList<>();
            cartLineInfoList.add(cartLineInfo);
            CartInfo cartInfo = new CartInfo();
            cartInfo.setCartLines(cartLineInfoList);
            Utils.setCartInSession(request, cartInfo);
            request.getSession().setAttribute("ProductCode", reservationInfo.getProductCode());
            request.getSession().setAttribute("IDReservation", reservationInfo.getId());
        }
        CartInfo cartInfo = Utils.getCartInSession(request);
        List<ProductInfo> productInfoList = new ArrayList<>();
        for (String code : cartInfo.updateQuantity(cartForm, productDAO)) {
            productInfoList.add(productDAO.findProductInfo(code));
        }
        if (cartInfo.updateQuantity(cartForm, productDAO).size() != 0) {
            request.getSession().setAttribute("listNotProduct", productInfoList);
            request.getSession().setAttribute("mismatch", true);
            return "redirect:/shoppingCart";
        }
        if (cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartConfirmation";
        }
        request.getSession().setAttribute("listNotProduct", null);
        return "redirect:/shoppingCart";
    }

    // GET: Show Cart
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);
        for (CartLineInfo cartLineInfo : myCart.getCartLines()) {
            if (discountDAO.getListDiscounts(cartLineInfo.getProductInfo().getCode()).size() != 0) {
                cartLineInfo.setDiscountInfoList(discountDAO.getListDiscounts(cartLineInfo.getProductInfo().getCode()));
            }
        }
        if (accountDAO.findAccount(request.getRemoteUser()).getDiscount() != null) {
            double discount = accountDAO.findAccount(request.getRemoteUser()).getDiscount();
            model.addAttribute("discount", discount);
        }
        if (request.getSession().getAttribute("listNotProduct") != null) {
            model.addAttribute("listNotProduct", request.getSession().getAttribute("listNotProduct"));
        }
        else {
            request.getSession().setAttribute("mismatch", false);
            model.addAttribute("listNotProduct", null);
        }
        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    @RequestMapping(value = { "/myShoppingCart" }, method = RequestMethod.GET)
    public String myShoppingCart(HttpServletRequest request, Model model, @RequestParam(value = "orderID") String orderId) {
        CartInfo myCart = new CartInfo();
        OrderInfo orderInfo = orderDAO.getOrderInfo(orderId);
        List<CartLineInfo> cartLines = new ArrayList<>();
        for (OrderDetailInfo orderDetailInfo : orderDAO.listOrderDetailInfos(orderId)) {
            CartLineInfo cartLineInfo = new CartLineInfo();
            if (discountDAO.getListDiscounts(orderDetailInfo.getProductCode()) != null) {
                cartLineInfo.setDiscountInfoList(discountDAO.getListDiscounts(orderDetailInfo.getProductCode()));
            }
            cartLineInfo.setQuantity(orderDetailInfo.getQuanity());
            cartLineInfo.setProductInfo(productDAO.findProductInfo(orderDetailInfo.getProductCode()));
            cartLines.add(cartLineInfo);
        }
        myCart.setCartLines(cartLines);
        myCart.setOrderNum(orderInfo.getOrderNum());
        if (orderInfo.getDelivery().equals("YES")) {
            myCart.setDeliveryYes(true);
        }
        if (orderInfo.getDelivery().equals("NO")) {
            myCart.setDeliveryYes(false);
        }
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setName(orderInfo.getCustomerName());
        customerInfo.setPhone(orderInfo.getCustomerPhone());
        customerInfo.setAddress(orderInfo.getCustomerAddress());
        customerInfo.setEmail(orderInfo.getCustomerEmail());
        myCart.setCustomerInfo(customerInfo);
        for (CartLineInfo cartLineInfo : myCart.getCartLines()) {
            if (discountDAO.getListDiscounts(cartLineInfo.getProductInfo().getCode()).size() != 0) {
                cartLineInfo.setDiscountInfoList(discountDAO.getListDiscounts(cartLineInfo.getProductInfo().getCode()));
            }
        }
        Utils.setCartInSession(request, myCart);
        model.addAttribute("cartForm", myCart);
        orderInfoForDelete = orderInfo;
        return "shoppingCart";
    }

    // GET: Enter customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

        CartInfo cartInfo = Utils.getCartInSession(request);

        // Cart is empty.
        if (cartInfo.isEmpty()) {

            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        }

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        if (customerInfo == null) {
            customerInfo = new CustomerInfo();
        }

        model.addAttribute("customerForm", customerInfo);
        model.addAttribute("name", name);
        return "shoppingCartCustomer";
    }

    // POST: Save customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request, //
                                           Model model, //
                                           @ModelAttribute("customerForm") @Validated CustomerInfo customerForm, //
                                           BindingResult result, //
                                           final RedirectAttributes redirectAttributes) {

        // If has Errors.
        if (result.hasErrors()) {
            customerForm.setValid(false);
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);

        cartInfo.setCustomerInfo(customerForm);

        if (request.getSession().getAttribute("reservationId") != null) {
            Reservation reservation = reservationDAO.findReservation((String) request.getSession().getAttribute("reservationId"));
            reservation.setEmail(customerForm.getEmail());
            Session session = sessionFactory.getCurrentSession();
            session.update(reservation);
            request.getSession().setAttribute("reservationId", null);
            return "redirect:/shoppingCart";
        }

        if ((Boolean) request.getSession().getAttribute("mismatch")) {
            return "redirect:/shoppingCart";
        }

        for (CartLineInfo cartLineInfo : cartInfo.getCartLines()) {
            if (cartLineInfo.getProductInfo().getCode().equals(request.getSession().getAttribute("ProductCode"))) {
                reservationDAO.delete((String) request.getSession().getAttribute("IDReservation"));
                break;
            }
        }

        return "redirect:/shoppingCartConfirmation";
    }

    // GET: Review Cart to confirm.
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        // Cart have no products.
        if (cartInfo.isEmpty()) {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            // Enter customer info.
            return "redirect:/shoppingCartCustomer";
        }

        if (accountDAO.findAccount(request.getRemoteUser()).getDiscount() != null) {
            double discount = accountDAO.findAccount(request.getRemoteUser()).getDiscount();
            model.addAttribute("discount", discount);
        }

        if (request.getSession().getAttribute("restatementOfBalance") == null) {
            for (CartLineInfo cartLineInfo : cartInfo.getCartLines()) {
                ProductInfo productInfo = productDAO.findProductInfo(cartLineInfo.getProductInfo().getCode());
                productInfo.setSurplus(productInfo.getSurplus() - cartLineInfo.getQuantity());
                productDAO.save(productInfo);
                request.getSession().setAttribute("restatementOfBalance", true);
            }
        }

        return "shoppingCartConfirmation";
    }

    // POST: Send Cart (Save).
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (accountDAO.findAccount(request.getRemoteUser()).getDiscount() != null) {
            double discount = accountDAO.findAccount(request.getRemoteUser()).getDiscount();
            model.addAttribute("discount", discount);
        }

        DeliveryInfo deliveryInfo = new DeliveryInfo();
        if (request.getParameter("delivery") != null && request.getParameter("delivery").equals("1")) {
            cartInfo.setDeliveryYes(true);
            request.getSession().setAttribute("myCart", cartInfo);
            request.getSession().setAttribute("deliveryYes", 1);
            request.getSession().setAttribute("DetailsOfDelivery", 1);
            return "shoppingCartConfirmation";
        }
        // Cart have no products.
        if (cartInfo.isEmpty()) {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            // Enter customer info.
            return "redirect:/shoppingCartCustomer";
        }
        if (request.getParameter("deliveryYes") == null ) {
            cartInfo.setDeliveryYes(false);
        }
        else if (request.getParameter("deliveryYes").equals("1")){
            deliveryInfo.setDate(request.getParameter("date"));
            deliveryInfo.setTime(request.getParameter("time"));
        }
        try {
            if (orderInfoForDelete != null) {
                orderDAO.delete(orderInfoForDelete);
                orderInfoForDelete = null;
            }
            orderDAO.saveOrder(cartInfo, deliveryInfo, request.getUserPrincipal().getName());
            accountDAO.saveTotal(accountDAO.findAccount(request.getUserPrincipal().getName()), cartInfo.getAmountTotal() - CartInfo.CostOfDelivery);
        } catch (Exception e) {
            // Need: Propagation.NEVER?
            return "shoppingCartConfirmation";
        }
        // Remove Cart In Session.
        Utils.removeCartInSession(request);

        // Store Last ordered cart to Session.
        Utils.storeLastOrderedCartInSession(request, cartInfo);

        // Redirect to successful page.

        return "redirect:/shoppingCartFinalize";
    }

    @RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {

        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }

        return "shoppingCartFinalize";
    }

    @RequestMapping(value = { "/productImage" }, method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Product product = null;
        if (code != null) {
            product = this.productDAO.findProduct(code);
        }
        if (product != null && product.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }

    @RequestMapping(value = { "/discounts" }, method = RequestMethod.GET)
    public String addAndEditDiscounts(HttpServletRequest request, Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        if(request.getParameter("add") != null && request.getParameter("add").equals("YES")) {
            model.addAttribute("add", "ON");
        }
        if(request.getParameter("edit") != null && request.getParameter("edit").equals("YES")) {
            model.addAttribute("edit", "ON");
        }
        if(request.getParameter("delete") != null) {
           discountDAO.delete(request.getParameter("delete"));
//           return "redirect:/discounts";
        }
        model.addAttribute("productInfo", productDAO.findProductInfo(code));
        model.addAttribute("listOfDiscounts", discountDAO.getListDiscounts(code));
        return "discounts";
    }

    @RequestMapping(value = { "/discounts" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String discountSave(HttpServletRequest request, Model model, @RequestParam(value = "code") String code) {
        if (request.getParameter("quantity_new") != null && request.getParameter("value_new") != null) {
            try {
                discountDAO.save(request.getParameter("quantity_new"), request.getParameter("value_new"), code);
            } catch (Exception e) {
                // Need: Propagation.NEVER?
                String errorMessage = e.getMessage();
                model.addAttribute("errorMessage", errorMessage);
                // Show product form.
                return "discounts";
            }
            model.addAttribute("productInfo", productDAO.findProductInfo(code));
            model.addAttribute("listOfDiscounts", discountDAO.getListDiscounts(code));
            return "discounts";
        }
        else {
            int i = 0;
            for (DiscountInfo discountInfo : discountDAO.getListDiscounts(code)) {
                if (discountInfo.getQuantity() != Integer.parseInt(request.getParameter("quantity" + i)) ||
                    discountInfo.getValue() != Integer.parseInt(request.getParameter("value" + i))) {
                        DiscountInfo discountInfo_tmp = new DiscountInfo();
                        discountInfo_tmp.setId(discountInfo.getId());
                        discountInfo_tmp.setProduct_code(code);
                        discountInfo_tmp.setQuantity(Integer.parseInt(request.getParameter("quantity" + i)));
                        discountInfo_tmp.setValue(Integer.parseInt(request.getParameter("value" + i)));
                        try {
                            discountDAO.update(discountInfo_tmp);
                        } catch (Exception e) {
                            // Need: Propagation.NEVER?
                            String errorMessage = e.getMessage();
                            model.addAttribute("errorMessage", errorMessage);
                            // Show product form.
                            return "discounts";
                        }
                }
                i++;
            }
        }
        model.addAttribute("productInfo", productDAO.findProductInfo(code));
        model.addAttribute("listOfDiscounts", discountDAO.getListDiscounts(code));
        return "discounts";
    }

    @RequestMapping(value = { "/buyReservationProduct" }, method = RequestMethod.GET)
    public String buyReservationProduct(Model model) {

        model.addAttribute("codeForReservation", "codeForReservation");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());
        model.addAttribute("userDetails", userDetails);

        return "accountInfo";
    }

    @RequestMapping(value = { "/registration" }, method = RequestMethod.POST)
    public String userRegistration(HttpServletRequest request, Model model) {
        if (request.getParameter("name").equals("")) {
            model.addAttribute("errorMessage", "Fill in the field Name!");
            return "registration";
        }
        model.addAttribute("name", request.getParameter("name"));
        if (request.getParameter("userName").equals("")) {
            model.addAttribute("errorMessage", "Fill in the field User Name!");
            return "registration";
        }
        model.addAttribute("userName", request.getParameter("userName"));
        if (request.getParameter("password").equals("")) {
            model.addAttribute("errorMessage", "Fill in the field Password!");
            return "registration";
        }
        if (!request.getParameter("password").matches("^[0-9a-zA-Z]{5,20}$")) {
            model.addAttribute("errorMessage", "The password must be at least 5 characters long, " +
                    "including letters and numbers, no more than 20 characters!");
            return "registration";
        }
        if (!request.getParameter("password").equals(request.getParameter("repeatPassword"))) {
            model.addAttribute("errorMessage", "Password must match!");
            return "registration";
        }
        if (!request.getParameter("password").equals(request.getParameter("repeatPassword"))) {
            model.addAttribute("errorMessage", "Password must match!");
            return "registration";
        }
        if (accountDAO.findAccount(request.getParameter("userName")) != null) {
            model.addAttribute("errorMessage", "This user already exists!");
            return "registration";
        }
        Account account = new Account();
        account.setUserName(request.getParameter("userName"));
        account.setActive(true);
        account.setPassword(request.getParameter("password"));
        account.setUserRole("EMPLOYEE");
        Session session = sessionFactory.getCurrentSession();
        session.persist(account);
        name = request.getParameter("name");
        return "login";
    }

    @RequestMapping(value = { "/regularCustomerDiscounts" }, method = RequestMethod.GET)
    public String addAndEditRegularCustomerDiscounts(HttpServletRequest request, Model model) {
        if(request.getParameter("add") != null && request.getParameter("add").equals("YES")) {
            model.addAttribute("add", "ON");
        }
        if(request.getParameter("edit") != null && request.getParameter("edit").equals("YES")) {
            model.addAttribute("edit", "ON");
        }
        if(request.getParameter("delete") != null) {
            regularCustomerDiscountDAO.delete(request.getParameter("delete"));
//           return "redirect:/discounts";
        }
        model.addAttribute("listOfDiscounts", regularCustomerDiscountDAO.getListDiscounts());
        return "regularCustomerDiscounts";
    }

    @RequestMapping(value = { "/regularCustomerDiscounts" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String saveRegularCustomerDiscounts(HttpServletRequest request, Model model) {
        if (request.getParameter("total_new") != null && request.getParameter("value_new") != null) {
            try {
                regularCustomerDiscountDAO.save(request.getParameter("total_new"), request.getParameter("value_new"));
            } catch (Exception e) {
                // Need: Propagation.NEVER?
                String errorMessage = e.getMessage();
                model.addAttribute("errorMessage", errorMessage);
                // Show product form.
                return "regularCustomerDiscounts";
            }
            model.addAttribute("listOfDiscounts", regularCustomerDiscountDAO.getListDiscounts());
            return "regularCustomerDiscounts";
        }
        else {
            int i = 0;
            for (RegularCustomerDiscountInfo regularCustomerDiscountInfo : regularCustomerDiscountDAO.getListDiscounts()) {
                if (regularCustomerDiscountInfo.getTotal() != Integer.parseInt(request.getParameter("total" + i)) ||
                        regularCustomerDiscountInfo.getValue() != Integer.parseInt(request.getParameter("total" + i))) {
                    RegularCustomerDiscountInfo regularCustomerDiscountInfo_tmp = new RegularCustomerDiscountInfo();
                    regularCustomerDiscountInfo_tmp.setId(regularCustomerDiscountInfo.getId());
                    regularCustomerDiscountInfo_tmp.setTotal(Integer.parseInt(request.getParameter("total" + i)));
                    regularCustomerDiscountInfo_tmp.setValue(Integer.parseInt(request.getParameter("value" + i)));
                    try {
                        regularCustomerDiscountDAO.update(regularCustomerDiscountInfo_tmp);
                    } catch (Exception e) {
                        // Need: Propagation.NEVER?
                        String errorMessage = e.getMessage();
                        model.addAttribute("errorMessage", errorMessage);
                        // Show product form.
                        return "regularCustomerDiscounts";
                    }
                }
                i++;
            }
        }
        model.addAttribute("listOfDiscounts", regularCustomerDiscountDAO.getListDiscounts());
        return "regularCustomerDiscounts";
    }
}
