package com.hitg.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hitg.dao.CountryDAO;
import com.hitg.domain.City;
import com.hitg.domain.Country;
import com.hitg.dto.CityCodeResponse;
import com.hitg.dto.CountriesResponse;
import com.hitg.dto.Quote;
import com.hitg.dto.QuotesResponseDTO;
import com.hitg.dto.TripAdviserResultDTO;

@Service("skyScannerService")
public class SkyScannerService {
	private static final String LOCALE = "en-GB";
	private static final String CURRENCY_CODE = "EUR";
	private static final String CITY_CODE_URL = "http://partners.api.skyscanner.net/apiservices/autosuggest/v1.0/";
	private static final String QUOTES_URL = "https://app.xapix.io/api/v1/ITravelBot/flight_quotes";
	private static final String REFERAL_URL = "http://partners.api.skyscanner.net/apiservices/referral/v1.0/{market}/{currency}/{locale}/{originPlace}/{destinationPlace}/{outboundPartialDate}/{inboundPartialDate}?apiKey={shortApiKey}";
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CountryDAO countryDAO;
	@Value("${bot.scyscanner.key}")
	private String API_KEY;

	public void getCountries() {
		CountriesResponse response = restTemplate.getForObject(
				"http://partners.api.skyscanner.net/apiservices/reference/v1.0/countries/en-GB?apiKey=prtl6749387986743898559646983194",
				CountriesResponse.class);
		response.getCountries().forEach(country -> countryDAO.save(country));
	}

	public City getCity(String name, Country market) {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("apiKey", API_KEY);
		params.add("query", name);
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(CITY_CODE_URL + market.getCode() + "/" + CURRENCY_CODE + "/" + LOCALE).queryParams(params);
		CityCodeResponse response = restTemplate.getForObject(builder.toUriString(), CityCodeResponse.class);
		if (!response.getPlaces().isEmpty()) {
			return response.getPlaces().stream().filter(
					place -> place.getPlaceName().equals(name) && place.getCountryName().equals(market.getName()))
					.findFirst().get();
		}
		return null;
	}

	public Double getMinQuotesPrice(String countryFromName, String cityFromName, String countryToName,
			String cityToName, LocalDate dateFrom, LocalDate dateTo) {
		Country countryFrom = countryDAO.findByName(countryFromName);
		Country countryTo = countryDAO.findByName(countryToName);
		City cityFrom = getCity(cityFromName, countryFrom);
		City cityTo = getCity(cityToName, countryTo);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("filter[market]", countryFrom.getCode());
		params.add("filter[locale]", LOCALE);
		params.add("filter[currency]", CURRENCY_CODE);
		params.add("filter[origin_place]", cityFrom.getPlaceId());
		params.add("filter[destination_place]", cityTo.getPlaceId());
		params.add("filter[outbound_partial_date]", dateFrom.format(DateTimeFormatter.ISO_DATE));
		params.add("filter[inbound_partial_date]", dateTo.format(DateTimeFormatter.ISO_DATE));
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(QUOTES_URL).queryParams(params);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("Authorization-Token", "ZLm4dqJSNi08zb7GHV1WB3OQDaCF6rTe");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String decodedURL = builder.toUriString().replaceAll("%5B", "[").replaceAll("%5D", "]");
		ResponseEntity<List<Quote>> response = restTemplate.exchange(decodedURL, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<Quote>>() {
				});
		if (response.getBody() != null && !response.getBody().isEmpty()) {
			return response.getBody().stream().mapToDouble(Quote::getMinPrice).min().getAsDouble();
		}
		return null;
	}

	public String getReferalUrl(String countryFromName, String cityFromName, String countryToName, String cityToName,
			LocalDate dateFrom, LocalDate dateTo) {
		Country countryFrom = countryDAO.findByName(countryFromName);
		Country countryTo = countryDAO.findByName(countryToName);
		City cityFrom = getCity(cityFromName, countryFrom);
		City cityTo = getCity(cityToName, countryTo);
		return REFERAL_URL.replace("{market}", countryFrom.getCode()).replace("{locale}", LOCALE)
				.replace("{currency}", CURRENCY_CODE).replace("{originPlace}", cityFrom.getPlaceId())
				.replace("{destinationPlace}", cityTo.getPlaceId())
				.replace("{outboundPartialDate}", dateFrom.format(DateTimeFormatter.ISO_DATE)).replace("{inboundPartialDate}",  dateTo.format(DateTimeFormatter.ISO_DATE)).replace("{shortApiKey}", API_KEY.substring(0, 16));

	}

}
