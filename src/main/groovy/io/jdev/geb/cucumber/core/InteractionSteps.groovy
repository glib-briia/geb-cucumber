package io.jdev.geb.cucumber.core

import cucumber.api.DataTable
import cucumber.api.Scenario
import geb.Module
import io.jdev.cucumber.variables.core.Decoder
import io.jdev.geb.cucumber.core.util.TableUtil
import io.jdev.geb.cucumber.core.util.ValueUtil

class InteractionSteps extends StepsBase {

    FieldFinder fieldFinder

    public void before (Scenario scenario, Binding binding, FieldFinder fieldFinder, Decoder variableDecoder) {
        super.before(scenario, binding, variableDecoder)
        this.fieldFinder = fieldFinder
    }

    void enter(String rawValue, String fieldDesc) {
        def value = variableScope.decodeVariable(rawValue)
        def field = fieldFinder.findField(fieldDesc, browser.page)
        ValueUtil.enterValue(field, value)
    }

    void enterValues(String fieldDesc, DataTable dataTable) {
        def target = getOptionalTarget(fieldDesc)
        List<Map<String,Object>> vals = TableUtil.dataTableToMaps(variableScope, dataTable, false)
        assert vals.size() == 1, "Cannot enter more than 1 row when entering multiple values"
        enterTargetValues(target, vals.first())
    }

    private def getOptionalTarget(String fieldDesc) {
        def target
        if(fieldDesc) {
            target = fieldFinder.findField(fieldDesc, browser.page)
            assert target instanceof Module, "$fieldDesc is not a module, but you cannot enter multiple values into non-modules"
        } else {
            target = browser.page
        }
        target
    }

    private void enterTargetValues(def target, Map<String,Object> vals) {
        for(Map.Entry<String,Object> entry : vals) {
            String fieldName = entry.key
            def field = fieldFinder.findField(fieldName, target)
            ValueUtil.enterValue(field, entry.value)
        }
    }

    void hasValue(boolean wait, String rawValue, String fieldDesc) {
        def value = variableScope.decodeVariable(rawValue)
        runWithOptionalWait(wait) {
            def field = fieldFinder.findField(fieldDesc, browser.page)
            ValueUtil.hasValue(field, value)
        }
    }


    void hasValues(boolean wait, String fieldDesc, DataTable dataTable) {
        List<Map<String,Object>> vals = TableUtil.dataTableToMaps(variableScope, dataTable, false)
        assert vals.size() == 1, "Cannot enter more than 1 row when verifying multiple values"
        runWithOptionalWait(wait) {
            def target = getOptionalTarget(fieldDesc)
            targetHasValues(target, vals.first())
        }
    }

    private void targetHasValues(def target, Map<String,Object> vals) {
        for(Map.Entry<String,Object> entry : vals) {
            String fieldName = entry.key
            def field = fieldFinder.findField(fieldName, target)
            ValueUtil.hasValue(field, entry.value)
        }
    }

    void click(String fieldDesc) {
        def field = fieldFinder.findField(fieldDesc, browser.page)
        field.click()
    }

    void isChecked(boolean wait, String fieldDesc) {
        assert runWithOptionalWait(wait) {
            def field = fieldFinder.findField(fieldDesc, browser.page)
            field.value() != false
        }
    }

    void isNotChecked(boolean wait, String fieldDesc) {
        assert runWithOptionalWait(wait) {
            def field = fieldFinder.findField(fieldDesc, browser.page)
            field.value() == false
        }
    }

    void isPresent(boolean wait, String fieldDesc) {
        assert runWithOptionalWait(wait) {
            def field = fieldFinder.findField(fieldDesc, browser.page)
            field
        }
    }

    void isNotPresent(boolean wait, String fieldDesc) {
        assert runWithOptionalWait(wait) {
            def field = fieldFinder.findField(fieldDesc, browser.page)
            !field
        }
    }

    void isVisible(boolean wait, String fieldDesc) {
        assert runWithOptionalWait(wait) {
            def field = fieldFinder.findField(fieldDesc, browser.page)
            field.displayed
        }
    }

    void isNotVisible(boolean wait, String fieldDesc) {
        assert runWithOptionalWait(wait) {
            def field = fieldFinder.findField(fieldDesc, browser.page)
            !field.displayed
        }
    }
}
