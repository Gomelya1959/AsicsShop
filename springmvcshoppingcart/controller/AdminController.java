package org.springmvcshoppingcart.controller;

import java.util.*;
import org.springmvcshoppingcart.dao.*;
import org.springmvcshoppingcart.entity.Account;
import org.springmvcshoppingcart.entity.Product;
import org.springmvcshoppingcart.entity.Reservation;
import org.springmvcshoppingcart.model.*;
import org.springmvcshoppingcart.validator.CourierInfoValidator;
import org.springmvcshoppingcart.validator.ProductInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
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
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
// Enable Hibernate Transaction.
@Transactional
// Need to use RedirectAttributes
@EnableWebMvc
public class AdminController {

    private ProductInfo productInfoForDelete = null;

    private CourierInfo courierInfoForDelete = null;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private DiscountDAO discountDAO;

    @Autowired
    private RegularCustomerDiscountDAO regularCustomerDiscountDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CourierDAO courierDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ProductInfoValidator productInfoValidator;

    @Autowired
    private CourierInfoValidator courierInfoValidator;

    @Autowired
    private ReservationDAO reservationDAO;

    // Configurated In ApplicationContextConfig.
    @Autowired
    private ResourceBundleMessageSource messageSource;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == ProductInfo.class) {
            dataBinder.setValidator(productInfoValidator);
            // For upload Image.
            dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        }
        else
            if (target.getClass() == CourierInfo.class) {
            dataBinder.setValidator(courierInfoValidator);
        }
    }

    @RequestMapping(value = { "/myOrders" }, method = RequestMethod.GET)
    public String myOrderView(Model model, HttpServletRequest request, @RequestParam("name_user") String name_user, @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        String userName = request.getRemoteUser();

        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }

        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;

        PaginationResult<OrderInfo> paginationResult = orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE, userName);
        if(paginationResult == null)
            return "redirect:/productList";
        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }

    // GET: Show Login Page
    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login(Model model) {

        return "login";
    }

    @RequestMapping(value = { "/accountInfo" }, method = RequestMethod.GET)
    public String accountInfo(Model model) {

        boolean enterInList = false;

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());

        Double total = accountDAO.findAccount(userDetails.getUsername()).getTotal();

        if (total != null) {
            Account account;
            double discount;
            for (RegularCustomerDiscountInfo regularCustomerDiscountInfo : regularCustomerDiscountDAO.getListDiscounts()) {
                if (total > regularCustomerDiscountInfo.getTotal()) {
                    discount = regularCustomerDiscountInfo.getValue();
                    account = accountDAO.findAccount(userDetails.getUsername());
                    account.setDiscount(discount);
                    accountDAO.updateAccount(account);
                    model.addAttribute("discount", discount);
                    enterInList = true;
                } else if (enterInList) {
                    break;
                }
            }
        }

        model.addAttribute("userDetails", userDetails);
        return "accountInfo";
    }

    @RequestMapping(value = { "/delivery" }, method = RequestMethod.GET)
    public String confirmDelivery(Model model, @RequestParam("orderID") String orderID, HttpServletRequest request) {
        OrderInfo orderInfo = null;
        if (orderID != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderID);
        }
        if (orderInfo == null) {
            return "redirect:/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderID);
        orderInfo.setDetails(details);
        orderInfo.setStatus(true);
        orderDAO.saveStatusOfDelivery(orderInfo.getId());
        model.addAttribute("orderInfo", orderInfo);
        return "order";
    }

    @RequestMapping(value = { "/return" }, method = RequestMethod.GET)
    public String confirmReturn(Model model, @RequestParam("orderID") String orderID, HttpServletRequest request) {
        OrderInfo orderInfo = null;
        if (orderID != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderID);
        }
        if (orderInfo == null) {
            return "redirect:/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderID);
        orderInfo.setDetails(details);
        orderDAO.saveStatusOfDelivery(orderInfo.getId());
        model.addAttribute("orderInfo", orderInfo);
        return "return";
    }

    @RequestMapping(value = { "/return" }, method = RequestMethod.POST)
    @Transactional(propagation = Propagation.NEVER)
    public String returnConfirm(Model model, @RequestParam("orderID") String orderID, HttpServletRequest request) {
        OrderInfo orderInfo = null;
        if (orderID != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderID);
        }
        if (orderInfo == null) {
            return "redirect:/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderID);
        List<OrderDetailInfo> details_tmp = new ArrayList<>();
        int i = 0;
        double amount = 0;
        double saving = 0;
        for (OrderDetailInfo orderDetailInfo : details) {
            Map<Integer, Integer> discountMap = new HashMap<>();
            if (discountDAO.getListDiscounts(orderDetailInfo.getProductCode()).size() != 0) {
                for (DiscountInfo discountInfo : discountDAO.getListDiscounts(orderDetailInfo.getProductCode())) {
                    discountMap.put(discountInfo.getQuantity(), discountInfo.getValue());
                }
            }
            if(!request.getParameter("product" + i).equals("")) {
                int newQuantity = orderDetailInfo.getQuanity() - Integer.parseInt(request.getParameter("product" + i));
                orderDetailInfo.setQuanity(newQuantity);
                orderDetailInfo.setAmount(getAmount(newQuantity, orderDetailInfo.getPrice(), discountMap));
                orderDetailInfo.setSaving(getSaving(newQuantity, orderDetailInfo.getPrice(), discountMap));
                Product product = productDAO.findProduct(orderDetailInfo.getProductCode());
                product.setSurplus(product.getSurplus() + Integer.parseInt(request.getParameter("product" + i)));
                productDAO.updateProduct(product);
            }
            details_tmp.add(orderDetailInfo);
            amount += orderDetailInfo.getAmount();
            saving += orderDetailInfo.getSaving();
            i++;
        }
        orderInfo.setDetails(details_tmp);
        orderInfo.setAmount(amount + CartInfo.CostOfDelivery);
        orderInfo.setSaving(saving);
        orderInfo.setStatus(true);
        accountDAO.saveTotal(accountDAO.findAccount(orderInfo.getUser_name()), amount - orderDAO.findOrder(orderID).getAmount() + CartInfo.CostOfDelivery);
        orderDAO.saveOrderWithReturn(orderInfo, details_tmp);
        model.addAttribute("orderInfo", orderInfo);
        return "order";
    }

    @RequestMapping(value = { "/orderList" }, method = RequestMethod.GET)
    public String orderList(Model model, //
                            @RequestParam(value = "page", defaultValue = "1") String pageStr, HttpServletRequest request) {
        String userName = request.getRemoteUser();
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;
        PaginationResult<OrderInfo> paginationResult = orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE, userName);

        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }

    // GET: Show product.
    @RequestMapping(value = { "/product" }, method = RequestMethod.GET)
    public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ProductInfo productInfo = null;

        if (code != null && code.length() > 0) {
            productInfo = productDAO.findProductInfo(code);
        }
        if (productInfo == null) {
            productInfo = new ProductInfo();
            productInfo.setNewProduct(true);
        }
        model.addAttribute("productForm", productInfo);
        return "product";
    }

    @RequestMapping(value = { "/courier" }, method = RequestMethod.GET)
    public String courier(Model model, @RequestParam(value = "id", defaultValue = "") String id) {
        CourierInfo courierInfo = null;

        if (id != null && id.length() > 0) {
            courierInfo = courierDAO.getCourierInfo(courierDAO.findCourier(id));
        }
        if (courierInfo == null) {
            courierInfo = new CourierInfo();
            courierInfo.setNewCourier(true);
        }
        model.addAttribute("courierForm", courierInfo);
        return "courier";
    }

    @RequestMapping(value = { "/requestOnDelete" }, method = RequestMethod.GET)
    public String requestOnDelete(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ProductInfo productInfo = productDAO.findProductInfo(code);
        productInfoForDelete = productInfo;
        model.addAttribute("deleteForm", productInfo);
        return "requestOnDelete";
    }

    @RequestMapping(value = { "/requestOnDeleteCourier" }, method = RequestMethod.GET)
    public String requestOnDeleteCourier(Model model, @RequestParam(value = "id", defaultValue = "") String id) {
        CourierInfo courierInfo = courierDAO.getCourierInfo(courierDAO.findCourier(id));
        courierInfoForDelete = courierInfo;
        model.addAttribute("deleteForm", courierInfo);
        return "requestOnDeleteCourier";
    }

    @RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
    @Transactional(propagation = Propagation.NEVER)
    public String productDelete(HttpServletRequest request) {
        if (request.getParameter("delete").equals("1")) {
            productDAO.delete(productInfoForDelete);
        }
        return "redirect:/productList";
    }

    @RequestMapping(value = { "/deleteCourier" }, method = RequestMethod.POST)
    @Transactional(propagation = Propagation.NEVER)
    public String courierDelete(HttpServletRequest request) {
        if (request.getParameter("delete").equals("1")) {
            courierDAO.delete(courierInfoForDelete);
        }
        return "redirect:/courierList";
    }

    @RequestMapping(value = { "/deleteImage" }, method = RequestMethod.GET)
    public String deleteImage(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ProductInfo productInfo = productDAO.findProductInfo(code);
        productInfo.setNotImage(true);
        productDAO.save(productInfo);
        return "redirect:/productList";
    }

    // POST: Save product
    @RequestMapping(value = { "/product" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String productSave(Model model, //
                              @ModelAttribute("productForm") @Validated ProductInfo productInfo, //
                              BindingResult result, //
                              final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "product";
        }
        try {
            productDAO.save(productInfo);
        } catch (Exception e) {
            // Need: Propagation.NEVER?
            String message = e.getMessage();
            model.addAttribute("errorMessage", message);
            // Show product form.
            return "product";

        }
        return "redirect:/productList";
    }

    @RequestMapping(value = { "/courier" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String courierSave(Model model, //
                              @ModelAttribute("courierForm") @Validated CourierInfo courierInfo, //
                              BindingResult result, //
                              final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "courier";
        }
        try {
            courierDAO.save(courierInfo);
        } catch (Exception e) {
            // Need: Propagation.NEVER?
            String message = e.getMessage();
            model.addAttribute("errorMessage", message);
            // Show product form.
            return "courier";

        }
        return "redirect:/courierList";
    }

    @RequestMapping(value = { "/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);
        if (orderDAO.findOrder(orderId) != null) {
            orderInfo.setStatus(orderDAO.findOrder(orderId).isStatus());
        }
        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }

    @RequestMapping(value = { "/deleteOrder" }, method = RequestMethod.GET)
    public String deleteOrder(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        model.addAttribute("orderForDelete", orderInfo);
        return "deleteOrder";
    }

    @RequestMapping(value = { "/deleteOrder" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String deleteOrder(HttpServletRequest request, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        if (request.getParameter("delete").equals("OK")) {
            orderDAO.delete(orderInfo);
        }
        if (request.getParameter("delete").equals("NO")) {
            return "redirect:/orderList";
        }
        return "redirect:/orderList";
    }

    @RequestMapping(value = { "/addProduct" }, method = RequestMethod.GET)
    public String addProduct(Model model, @RequestParam("code") String code) {
        ProductInfo productInfo = null;
        if (code != null) {
            productInfo = this.productDAO.findProductInfo(code);
        }
        model.addAttribute("productInfo", productInfo);
        return "addProduct";
    }

    @RequestMapping(value = { "/addProduct" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String addProduct(HttpServletRequest request, @RequestParam("code") String code) {
        ProductInfo productInfo = null;
        if (code != null) {
            productInfo = this.productDAO.findProductInfo(code);
        }
        try {
            if (!request.getParameter("numbers").equals("")) {
                productInfo.setSurplus(Integer.parseInt(request.getParameter("numbers")) + productInfo.getSurplus());
                productDAO.save(productInfo);
                reservationDAO.sendingNotifications(productInfo);
            }
        }
        catch (NullPointerException e) {
            return "redirect:/productList";
        }
        return "redirect:/productList";
    }

    @RequestMapping(value = { "/orderByNum" }, method = RequestMethod.GET)
    public String orderViewByNum(Model model, @RequestParam("orderNum") String orderNum) {
        OrderInfo orderInfo = null;
        if (orderNum != null) {
            orderInfo = this.orderDAO.findByNum(Integer.parseInt(orderNum));
        }
        if (orderInfo == null) {
            return "redirect:/orderList";
        }
        if (orderInfo.getId() != null) {
            List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderInfo.getId());
            orderInfo.setDetails(details);
        }

        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }

    @RequestMapping(value = { "/reservation" }, method = RequestMethod.GET)
    public String productReservation(HttpServletRequest request, @RequestParam("code") String code,
                                     Model model) {
        if (request.getParameter("code") != null) {
            model.addAttribute("productInfo", productDAO.findProductInfo(code));
        }
        return "reservation";
    }

    @RequestMapping(value = { "/reservation" }, method = RequestMethod.POST)
    @Transactional(propagation = Propagation.NEVER)
    public String productReservation(HttpServletRequest request, @RequestParam("code") String code) {
        String user = request.getRemoteUser();
        Reservation reservation = new Reservation();
        if (request.getParameter("numbersOfReservedProducts") != null) {
            reservation = reservationDAO.save(new Date(), user, code, Integer.parseInt(request.getParameter("numbersOfReservedProducts")));
        }
        request.getSession().setAttribute("reservationId", reservation.getId());
        return "redirect:/shoppingCartCustomer";
    }

    private double getAmount(int quantity, double priceProduct, Map<Integer, Integer> discountMap) {
        Map.Entry<Integer, Integer> discountEntry_tmp = null;
        if (discountMap.size() == 0) {
            return quantity * priceProduct;
        }
        for (Map.Entry<Integer, Integer> discountEntry : discountMap.entrySet()) {
            int first = discountEntry.getKey();
            if (quantity < first) {
                return priceProduct * quantity;
            }
            if (quantity == discountEntry.getKey()) {
                return priceProduct * quantity * (100 - discountEntry.getValue())/100;
            }
            discountEntry_tmp = discountEntry;
        }
        return priceProduct * quantity * (100 - discountEntry_tmp.getValue())/100;
    }

    private double getSaving(int quantity, double priceProduct, Map<Integer, Integer> discountMap) {
        return quantity * priceProduct - getAmount(quantity, priceProduct, discountMap);
    }

}
